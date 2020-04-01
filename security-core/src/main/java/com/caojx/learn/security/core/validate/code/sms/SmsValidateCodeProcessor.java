package com.caojx.learn.security.core.validate.code.sms;

import com.caojx.learn.security.core.properties.SecurityConstants;
import com.caojx.learn.security.core.validate.code.ValidateCode;
import com.caojx.learn.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码处理器
 *
 * @author caojx
 * @version $Id: SmsValidateCodeProcessor.java,v 1.0 2020/3/1 2:41 下午 caojx
 * @date 2020/3/1 2:41 下午
 */
@Component("smsValidateCodeProcessor")
public class SmsValidateCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {
    /**
     * 短信验证码发送器
     */
    @Autowired
    private SmsCodeSender smsCodeSender;

    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        String paramName = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), paramName);

        // 验证码发送给对应的手机号
        smsCodeSender.send(mobile, validateCode.getCode());
    }
}