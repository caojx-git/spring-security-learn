package com.caojx.learn.security.core.validate.code.sms;

import com.caojx.learn.security.core.properties.SecurityProperties;
import com.caojx.learn.security.core.validate.code.ValidateCode;
import com.caojx.learn.security.core.validate.code.ValidateCodeGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码生成实现
 *
 * @author caojx
 * @version $Id: SmsValidateCodeGenerator.java,v 1.0 2020/2/22 6:37 下午 caojx
 * @date 2020/2/22 6:37 下午
 */
@Component("smsValidateCodeGenerator")
public class SmsValidateCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ValidateCode generate(ServletWebRequest servletWebRequest) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }
}