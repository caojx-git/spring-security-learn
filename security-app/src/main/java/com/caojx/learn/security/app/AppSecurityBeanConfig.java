package com.caojx.learn.security.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * app bean相关配置
 *
 * @author caojx created on 2020/3/21 3:37 下午
 */
@Configuration
public class AppSecurityBeanConfig extends WebSecurityConfigurerAdapter {

    /**
     * 不配置AuthenticationManager bean则 {@link com.caojx.learn.security.app.ImoocAuthorizationServerConfig} 没法注入该实例
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}