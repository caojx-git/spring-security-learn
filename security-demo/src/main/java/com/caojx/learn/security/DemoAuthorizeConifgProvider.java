package com.caojx.learn.security;

import com.caojx.learn.security.core.authorize.AuthorizeConfigProvider;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * demo项目自己的授权配置
 *
 * @author caojx created on 2020/3/26 10:20 下午
 */
//@Component
@Order(Integer.MAX_VALUE)
public class DemoAuthorizeConifgProvider implements AuthorizeConfigProvider {

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
//        config.antMatchers("/user","/demo.html").hasRole("ADMIN");
        // 使用自定义的方法校验权限，@bean名称.方法(参数)
        config.anyRequest().access("@rbacService.hasPermission(request, authentication)");

//        config.antMatchers("/order3").permitAll();
        return false;
//        return true; //有anyRequest配置
    }
}