package com.caojx.learn.security.app;

import com.caojx.learn.security.app.social.openid.OpenIdAuthenticationSecurityConfig;
import com.caojx.learn.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.caojx.learn.security.core.authorize.AuthorizeConfigManager;
import com.caojx.learn.security.core.properties.SecurityConstants;
import com.caojx.learn.security.core.properties.SecurityProperties;
import com.caojx.learn.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 资源服务器
 *
 * @author caojx
 * @version $Id: ImoocResourceServerConfig.java,v 1.0 2020/3/13 12:49 下午 caojx
 * @date 2020/3/13 12:49 下午
 */
@Configuration
@EnableResourceServer
public class ImoocResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    protected AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private SpringSocialConfigurer imoocSocialSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.formLogin() //表单登录类型，授权时会跳转到登录页面
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL) //需要身份认证时跳转的路径
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM) // 自定义处理登录请求URL，让Username Password Authentication Filter过滤器能处理，默认的是/login
                .successHandler(imoocAuthenticationSuccessHandler) //指定自定义的登录成功处理器
                .failureHandler(imoocAuthenticationFailureHandler); //指定自定义的登录失败处理器
        http
                .apply(validateCodeSecurityConfig) // 验证码相关的配置
                .and()
                .apply(smsCodeAuthenticationSecurityConfig) // 短信登录相关配置代码
                .and()
                .apply(imoocSocialSecurityConfig) // 社交登录相关配置
                .and()
                .apply(openIdAuthenticationSecurityConfig) //简单模式社交登录
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
//                        "/user/regist", "/social/signUp"
//                ).permitAll() // 访问这些请求不需要身份认证
//                .anyRequest()   // 其他任何请求
//                .authenticated() // 都需要身份认证
//                .and()
                .csrf().disable(); // 关闭跨站请求伪造防护

        authorizeConfigManager.config(http.authorizeRequests());

    }
}