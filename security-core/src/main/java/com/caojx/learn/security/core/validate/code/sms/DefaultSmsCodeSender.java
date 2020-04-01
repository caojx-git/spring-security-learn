package com.caojx.learn.security.core.validate.code.sms;


/**
 * 默认的短信验证码实现，不同的应用可以选不同的短信发送供应商，这里只提供默认实现，可以覆盖
 *
 * @author caojx
 * @version $Id: DefaultSmsCodeSender.java,v 1.0 2020/2/22 10:27 下午 caojx
 * @date 2020/2/22 10:27 下午
 */
public class DefaultSmsCodeSender implements SmsCodeSender {
    @Override
    public void send(String mobile, String code) {
        // 假设发送（需要接短信通道供应商接口），这里调用调用短信通道发送
        System.out.println("请配置真实的短信验证码发送器(SmsCodeSender)");

        System.out.println("向手机" + mobile + "发送验证码" + code);
    }
}