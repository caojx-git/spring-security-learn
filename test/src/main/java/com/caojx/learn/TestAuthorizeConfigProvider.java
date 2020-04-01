package com.caojx.learn;

import com.caojx.learn.security.core.authorize.AuthorizeConfigProvider;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 类注释，描述
 *
 * @author caojx created on 2020/3/27 5:12 下午
 */
//@Component
public class TestAuthorizeConfigProvider implements AuthorizeConfigProvider {
    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(HttpMethod.POST, "/user/register").permitAll();
        return false; // 没有anyRequest配置
    }
}