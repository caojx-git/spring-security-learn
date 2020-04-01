package com.caojx.learn.security.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * app安全配置类
 *
 * @author caojx
 * @version $Id: AppSecurityConfig.java,v 1.0 2020/3/12 10:26 下午 caojx
 * @date 2020/3/12 10:26 下午
 */
//@Configuration
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()   //指定身份认证的方式为表单登录
                    .and()
                .authorizeRequests() //对请求授权
                .anyRequest()        //任何请求
                .authenticated()    //都需要安全认证
                    .and()
                .userDetailsService(userDetailsService);
    }
}