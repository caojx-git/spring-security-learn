package com.caojx.learn.security.browser;

import com.caojx.learn.security.browser.session.ImoocExpiredSessionStrategy;
import com.caojx.learn.security.core.authentication.PasswordAuthenticationSecurityConfig;
import com.caojx.learn.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.caojx.learn.security.core.authorize.AuthorizeConfigManager;
import com.caojx.learn.security.core.properties.SecurityConstants;
import com.caojx.learn.security.core.properties.SecurityProperties;
import com.caojx.learn.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 安全配置类
 * 注意：注释掉security-demoe中的com.caojx.learn.web.config.SecurityConfig类，
 * 我们会在security-browser模块中新增相关配置类com.caojx.learn.security.browser.BrowSerSecurityConfig，避免冲突
 *
 * @author caojx
 * @version $Id: BrowSerSecurityConfig.java,v 1.0 2020/2/19 5:41 下午 caojx
 * @date 2020/2/19 5:41 下午
 */
@Configuration
public class BrowSerSecurityConfig extends PasswordAuthenticationSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

//    @Autowired
//    private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;
//
//    @Autowired
//    private AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    @Autowired
    private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private SpringSocialConfigurer imoocSocialSecurityConfig;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    /**
     * Spring Security中使用org.springframework.security.crypto.password.PasswordEncoder，来处理密码的加密解密
     *
     * @return
     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    /**
     * 记住我功能需要的 PersistentTokenRepository
     *
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
//        tokenRepository.setCreateTableOnStartup(true); // JdbcTokenRepositoryImpl 有创建表的语句，设置在启动的时候执行，自动创建表，之后启动需要把这句话注释掉
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        applyPasswordAuthenticationConfig(http); //密码登录相关配置代码

        http.apply(smsCodeAuthenticationSecurityConfig) // 短信登录相关配置代码
                    .and()
                .apply(validateCodeSecurityConfig) // 验证码相关的配置
                    .and()
                .apply(imoocSocialSecurityConfig) // 社交登录相关配置
                    .and()  // 记住我功能配置
                .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds()) // 记住我有效时间
                    .userDetailsService(userDetailsService)
                    .and()
//                .sessionManagement() // session管理配置
//                    .invalidSessionUrl("/session/invalid") //session失效请求的地址
//                    .maximumSessions(1) // 最大的session数量为1，那么同一个用户，他后面登录的session就会把之前登录所产生的这个session给失效掉
//                    .expiredSessionStrategy(new ImoocExpiredSessionStrategy())
//                    .maxSessionsPreventsLogin(true) // 系统里用户已经有一个活动的session，你就不能在别的地方再登录，即当session数量达到最大以后，阻止后来的登录的这种行为
//                    .and()
//                    .and()
                .sessionManagement() // session管理配置
                    .invalidSessionStrategy(invalidSessionStrategy) //session失效处理策略，同时支持html与json格式数据响应
                    .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions()) // 最大的session数量配置
                    .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin()) // 系统里用户已经有一个活动的session，是否允许在别的地方再登录，即当session数量达到最大以后，阻止后来的登录的这种行为
                    .expiredSessionStrategy(sessionInformationExpiredStrategy) //session失效处理策略，同时支持html与json格式数据响应
                    .and()
                    .and()
                .logout() //退出登录相关配置
                    .logoutUrl("/signOut") // 退出请求地址，默认是/logout
//                    .logoutSuccessUrl("/imooc-logout.html") //退出成功跳转的路径
                    .logoutSuccessHandler(logoutSuccessHandler) //logoutSuccessHandler 与 logoutSuccessUrl 互斥，如果配置了logoutSuccessHandler，则logoutSuccessUrl就不会生效
                    .deleteCookies("JSESSIONID") //退出登录成功后，删除指定的cookie的名字
                    .and()
//                .authorizeRequests() // 请求授权配置
//                .antMatchers(
//                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
//                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
//                        securityProperties.getBrowser().getLoginPage(),
//                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
//                        securityProperties.getBrowser().getSignUpUrl(),
//                        securityProperties.getBrowser().getSession().getSessionInvalidUrl(),
//                        securityProperties.getBrowser().getSignOutUrl(),
//                        "/user/regist"
//                    ).permitAll() // 访问这些请求不需要身份认证
//                .antMatchers(HttpMethod.GET, "/user/*").hasRole("ADMIN")
////                .antMatchers(HttpMethod.GET, "/user/*").access("hasRole('ADMIN') and hasIpAddress('xxxx')")
//                .anyRequest()   // 其他任何请求
//                .authenticated() // 都需要身份认证
//                    .and()
                .csrf().disable(); // 关闭跨站请求伪造防护


            authorizeConfigManager.config(http.authorizeRequests());

    }

//        @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        // 图片验证码过滤器
//        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
//        validateCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
//        validateCodeFilter.setSecurityProperties(securityProperties);
//        validateCodeFilter.afterPropertiesSet();
//
//        // 短信验证码过滤器
//        SmsValidateCodeFilter smsValidateCodeFilter = new SmsValidateCodeFilter();
//        smsValidateCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
//        smsValidateCodeFilter.setSecurityProperties(securityProperties);
//        smsValidateCodeFilter.afterPropertiesSet();
//
//        http.addFilterBefore(smsValidateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 在UsernamePasswordAuthenticationFilter过滤器之前添加短信验证码过滤器
//                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 在UsernamePasswordAuthenticationFilter过滤器之前添加图形验证码过滤器
//                .formLogin() // 表单登录类型，授权时会跳转到登录页面
////        http.httpBasic()  // basic类型，授权时会弹出登录框
////                .loginPage("/imooc-signIn.html") // 指定需要身份认跳转到自定义的登录页面
//                .loginPage("/authentication/require") //需要身份认证时跳转的路径
//                .loginProcessingUrl("/authentication/form") // 自定义处理登录请求URL，让Username Password Authentication Filter过滤器能处理，默认的是/login
////                .successHandler(imoocAuthenticationSuccessHandler) // 指定自定义的登录成功处理器
////                .failureHandler(imoocAuthenticationFailureHandler) // 指定自定义的登录失败处理器
//                .successHandler(imoocAuthenticationSuccessHandler) // 指定自定义的登录成功处理器
//                .failureHandler(imoocAuthenticationFailureHandler) // 指定自定义的登录失败处理器
//
//                // 添加SocialAuthenticationFilter 过滤器
//                .and()
//                .apply(imoocSocialSecurityConfig)
//
//                // 记住我配置
//                .and()
//                .rememberMe()
//                .tokenRepository(persistentTokenRepository())
//                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds()) // 记住我有效时间
//                .userDetailsService(userDetailsService)
//
//                .and()
//                .authorizeRequests() // 请求授权配置
////                .antMatchers("/authentication/require").permitAll() //访问这些请求不需要身份认证
//                .antMatchers(
//                        "/authentication/require",
//                        "/authentication/mobile",
//                        "/code/image",
//                        "/code/sms",
//                        securityProperties.getBrowser().getLoginPage()
//                        ).permitAll() //访问这些请求不需要身份认证
//                .anyRequest()   // 任何请求
//                .authenticated() // 都需要身份认证
//                .and().csrf().disable() // 关闭跨站请求伪造防护
//                .apply(smsCodeAuthenticationSecurityConfig); // 直接把配置类加入到配置
//    }
}