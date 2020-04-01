package com.caojx.learn.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;

/**
 * SocialAuthenticationFilter后处理器，用于在不同环境下个性化社交登录的配置
 *
 * @author caojx
 * @version $Id: SocialAuthenticationFilterPostProcessor.java,v 1.0 2020/3/16 10:55 下午 caojx
 * @date 2020/3/16 10:55 下午
 */
public interface SocialAuthenticationFilterPostProcessor {

    /**
     * @param socialAuthenticationFilter 社交认证过滤器
     */
    void process(SocialAuthenticationFilter socialAuthenticationFilter);
}