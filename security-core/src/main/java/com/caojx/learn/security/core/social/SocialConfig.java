package com.caojx.learn.security.core.social;

import com.caojx.learn.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * Spring Social 配置
 *
 * @author caojx
 * @version $Id: SocialConfig.java,v 1.0 2020/2/29 8:33 下午 caojx
 * @date 2020/2/29 8:33 下午
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * required = false，因为有可能项目没有提供 connectionSignUp
     */
    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    @Autowired(required = false)
    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    /**
     * JdbcUsersConnectionRepository 配置
     * <p>
     * UsersConnectionRepository 存储器，真正我们在代码里用到的实现类叫做 JdbcUserConnectionRepository，
     * 这个类的一个作用就是针对我们数据库里面UserConnection表去做一些增删改查的操作
     * <p>
     * 由于我们修改了UserConnection表的rank字段为rank_1，所以重写了JdbcUsersConnectionRepository为MyJdbcUsersConnectionRepository
     *
     * @param connectionFactoryLocator
     * @return
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        // 数据源，connectionFactory, 加解密工具， Encryptors.noOpText() 暂时不做加解密
//       return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        MyJdbcUsersConnectionRepository repository = new MyJdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
//        repository.setTablePrefix("imooc_");
        if (connectionSignUp != null) { // 当connectionSignUp存在的时候，在使用自定义的注册逻辑
            repository.setConnectionSignUp(connectionSignUp);
        }
        return repository;
    }

    /**
     * Spring Social  SocialAuthenticationFilter 过滤器配置
     * <p>
     * SpringSocialConfigurer其实生成了一个SocialAuthenticationFilter，而这个Filter默认只会拦截/auth开头的请求路径
     *
     * @return
     */
    @Bean
    public SpringSocialConfigurer imoocSocialSecurityConfig() {
//        return new SpringSocialConfigurer();

        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
        ImoocSpringSocialConfigurer configurer = new ImoocSpringSocialConfigurer(filterProcessesUrl);
        // 指定注册页
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        // 设置后处理器
        configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
        return configurer;
    }

    /**
     * 这个工具类主要解决两个问题，
     * 第一个在注册过程中，如何拿到Spring Social的信息；第二个注册完成了，如何把业务系统的用户userId传给Spring Social。
     *
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator, getUsersConnectionRepository(connectionFactoryLocator));
    }

}