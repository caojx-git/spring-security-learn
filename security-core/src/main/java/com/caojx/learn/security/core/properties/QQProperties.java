package com.caojx.learn.security.core.properties;

import lombok.Data;

/**
 * 类注释，描述
 *
 * @author caojx
 * @version $Id: QQProperties.java,v 1.0 2020/2/29 9:12 下午 caojx
 * @date 2020/2/29 9:12 下午
 */
@Data
public class QQProperties extends com.caojx.learn.security.core.social.SocialProperties {

    /**
     * 服务提供商标识，默认是qq
     */
    private String providerId = "qq";

}