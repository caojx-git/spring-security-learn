package com.caojx.learn.security.core.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 默认的授权配置管理器，用于收集系统中所有 AuthorizeConfigProvider 并加载其配置
 *
 * @author caojx created on 2020/3/26 10:12 下午
 */
@Component
public class ImoocAuthorizeConfigManager implements AuthorizeConfigManager {

    /**
     * AuthorizeConfigProvider有顺序要求，所有要用List有序即可
     */
    @Autowired
    private Set<AuthorizeConfigProvider> authorizeConfigProviderList;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        boolean existAnyRequestConfig = false;
        String existAnyRequestConfigName = null;

        // 循环所有的授权配置提供器
        for (AuthorizeConfigProvider authorizeConfigProvider : authorizeConfigProviderList) {

            boolean currentIsAnyRequestConfig = authorizeConfigProvider.config(config);

            if (existAnyRequestConfig && currentIsAnyRequestConfig) { // 如果已经存在anyRequest配置，且当前配置也是anyRequest配置，那么抛出异常
                throw new RuntimeException("重复的anyRequest配置:" + existAnyRequestConfigName + "," + authorizeConfigProvider.getClass().getSimpleName());
            } else if (currentIsAnyRequestConfig) { // 如果当前配置有anyRequest配置，那么把existAnyRequestConfig置为true，标识已经有了anyRequest配置
                existAnyRequestConfig = true;
                existAnyRequestConfigName = authorizeConfigProvider.getClass().getSimpleName();
            }
        }

        // 如果系统中没有配置anyRequest，那么增加一个anyRequest
        if (!existAnyRequestConfig) {
            config.anyRequest().authenticated();
        }

        // 剩下的所有请求，都需要身份认证
//        config.anyRequest().authenticated();
    }

}