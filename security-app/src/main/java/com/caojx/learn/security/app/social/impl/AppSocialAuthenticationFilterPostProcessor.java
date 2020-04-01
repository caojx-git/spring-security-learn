package com.caojx.learn.security.app.social.impl;

import com.caojx.learn.security.core.social.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * APP社交登录成功后的处理器配置
 *
 * @author caojx
 * @version $Id: AppSocialAuthenticationFilterPostProcessor.java,v 1.0 2020/3/16 10:57 下午 caojx
 * @date 2020/3/16 10:57 下午
 */
@Component
public class AppSocialAuthenticationFilterPostProcessor implements SocialAuthenticationFilterPostProcessor {

    @Autowired
    private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Override
    public void process(SocialAuthenticationFilter socialAuthenticationFilter) {
        // 社交登录成功后，设置登录成功的处理器
        socialAuthenticationFilter.setAuthenticationSuccessHandler(imoocAuthenticationSuccessHandler);
    }
}