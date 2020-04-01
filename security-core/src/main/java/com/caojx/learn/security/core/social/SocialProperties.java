package com.caojx.learn.security.core.social;

import lombok.Data;

/**
 * Springboot autoconfigure 中2.x 不存在SocialAutoConfigurerAdapter、SocialProperties和SocialWebAutoConfiguration，直接将springboot autoconfigure 1.5.x中这三个类拷贝到我们项目里
 *
 * @author caojx
 * @version $Id: SocialProperties.java,v 1.0 2020/2/29 9:41 下午 caojx
 * @date 2020/2/29 9:41 下午
 */
public abstract class SocialProperties {
    private String appId;
    private String appSecret;

    public SocialProperties() {
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
