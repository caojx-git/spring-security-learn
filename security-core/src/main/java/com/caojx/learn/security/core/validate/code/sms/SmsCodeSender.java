package com.caojx.learn.security.core.validate.code.sms;

/**
 * 短信验证码发送接口
 *
 * @author caojx
 * @version $Id: SmsCodeSender.java,v 1.0 2020/2/22 10:26 下午 caojx
 * @date 2020/2/22 10:26 下午
 */
public interface SmsCodeSender {

    void send(String mobile, String code);
}