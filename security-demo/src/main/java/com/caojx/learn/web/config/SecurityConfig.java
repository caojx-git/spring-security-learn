package com.caojx.learn.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 配置不需要登录验证
 *
 * @author caojx
 * @version $Id: SecurityConfig.java,v 1.0 2020/2/16 11:01 下午 caojx
 * @date 2020/2/16 11:01 下午
 */
//@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().anyRequest().permitAll().and().logout().permitAll(); //配置不需要登录验证
//    }
}