package com.caojx.learn.security.core.social.qq.config;


import com.caojx.learn.security.core.properties.QQProperties;
import com.caojx.learn.security.core.properties.SecurityProperties;
import com.caojx.learn.security.core.social.SocialAutoConfigurerAdapter;
import com.caojx.learn.security.core.social.qq.connect.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * 创建 ConnectionFactory
 * ConditionalOnProperty的作用 当imooc.security.social.qq.app-id 配置的时候，这个类才生效
 *
 * @author caojx
 * @version $Id: QQAutoConfig.java,v 1.0 2020/2/29 9:29 下午 caojx
 * @date 2020/2/29 9:29 下午
 */
@Configuration
@ConditionalOnProperty(prefix = "imooc.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        QQProperties qqProperties = securityProperties.getSocial().getQq();
        return new QQConnectionFactory(qqProperties.getProviderId(), qqProperties.getAppId(), qqProperties.getAppSecret());
    }
}