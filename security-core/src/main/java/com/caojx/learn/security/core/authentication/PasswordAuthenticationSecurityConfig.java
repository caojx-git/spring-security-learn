package com.caojx.learn.security.core.authentication;

import com.caojx.learn.security.core.properties.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 密码登录相关配置代码
 *
 * @author caojx
 * @version $Id: PasswordAuthenticationSecurityConfig.java,v 1.0 2020/3/1 8:33 下午 caojx
 * @date 2020/3/1 8:33 下午
 */
public class PasswordAuthenticationSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Autowired
    protected AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    protected void applyPasswordAuthenticationConfig(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin() //表单登录类型，授权时会跳转到登录页面
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL) //需要身份认证时跳转的路径
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM) // 自定义处理登录请求URL，让Username Password Authentication Filter过滤器能处理，默认的是/login
                .successHandler(imoocAuthenticationSuccessHandler) //指定自定义的登录成功处理器
                .failureHandler(imoocAuthenticationFailureHandler); //指定自定义的登录失败处理器
    }
}