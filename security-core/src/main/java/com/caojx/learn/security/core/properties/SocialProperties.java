package com.caojx.learn.security.core.properties;

import lombok.Data;

/**
 * 社交登录属性配置类
 *
 * @author caojx
 * @version $Id: SocialProperties.java,v 1.0 2020/2/29 9:17 下午 caojx
 * @date 2020/2/29 9:17 下午
 */
@Data
public class SocialProperties extends com.caojx.learn.security.core.social.SocialProperties {

    /**
     * 社交登录，SocialAuthenticationFilter 过滤器默认拦截处理的url
     */
    private String filterProcessesUrl = "/auth";


    private QQProperties qq = new QQProperties();

    private WeixinProperties weixin = new WeixinProperties();

}