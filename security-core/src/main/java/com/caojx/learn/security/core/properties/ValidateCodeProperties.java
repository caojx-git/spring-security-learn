package com.caojx.learn.security.core.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 最后短信验证码和图形验证码的配置都会放到这里，所以对图形验证码配置再封一级
 *
 * @author caojx
 * @version $Id: ValidateCodeProperties.java,v 1.0 2020/2/22 5:38 下午 caojx
 * @date 2020/2/22 5:38 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateCodeProperties {

    private ImageCodeProperties image = new ImageCodeProperties();

    private SmsCodeProperties sms = new SmsCodeProperties();
}