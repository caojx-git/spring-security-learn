package com.caojx.learn.security.core.properties;

import com.caojx.learn.security.core.social.SocialProperties;
import lombok.Data;

/**
 * 微信properties配置类
 *
 * @author caojx
 * @version $Id: WeixinProperties.java,v 1.0 2020/3/6 12:53 下午 caojx
 * @date 2020/3/6 12:53 下午
 */
@Data
public class WeixinProperties extends SocialProperties {

    /**
     * 第三方id，用来决定发起第三方登录的url，默认是 weixin。
     */
    private String providerId = "weixin";

}