package com.caojx.learn.security.core.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信验证码默认配置
 *
 * @author caojx
 * @version $Id: ImageCodeProperties.java,v 1.0 2020/2/22 5:37 下午 caojx
 * @date 2020/2/22 5:37 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsCodeProperties {

    private int length = 6;
    private int expireIn = 60;

    /**
     * 逗号隔开的url，这些url需要做图形验证码校验
     */
    private String url;
}