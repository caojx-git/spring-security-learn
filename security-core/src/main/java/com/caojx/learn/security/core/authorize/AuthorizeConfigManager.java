package com.caojx.learn.security.core.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 授权信息管理器，用于收集系统中所有 AuthorizeConfigProvider 并加载其配置
 *
 * @author caojx created on 2020/3/26 10:12 下午
 */
public interface AuthorizeConfigManager {

    /**
     * @param config
     */
    void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);
}