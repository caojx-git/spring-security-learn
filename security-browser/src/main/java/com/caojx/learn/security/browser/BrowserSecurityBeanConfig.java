package com.caojx.learn.security.browser;

import com.caojx.learn.security.browser.logout.ImoocLogoutSuccessHandler;
import com.caojx.learn.security.browser.session.ImoocExpiredSessionStrategy;
import com.caojx.learn.security.browser.session.ImoocInvalidSessionStrategy;
import com.caojx.learn.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * 浏览器相关bean配置
 *
 * @author caojx
 * @version $Id: BrowserSecurityBeanConfig.java,v 1.0 2020/3/8 6:21 下午 caojx
 * @date 2020/3/8 6:21 下午
 */
@Configuration
public class BrowserSecurityBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * session失效策略bean声明
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy() {
        return new ImoocInvalidSessionStrategy(securityProperties.getBrowser().getSession().getSessionInvalidUrl());
    }

    /**
     * ExpiredSessionStrategy bean声明
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new ImoocExpiredSessionStrategy(securityProperties.getBrowser().getSession().getSessionInvalidUrl());
    }

    /**
     * 退出登录成功的处理器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new ImoocLogoutSuccessHandler(securityProperties.getBrowser().getSignOutUrl());
    }
}