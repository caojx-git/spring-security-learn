package com.caojx.learn.security.core.validate.code;

import com.caojx.learn.security.core.properties.SecurityProperties;
import com.caojx.learn.security.core.validate.code.image.ImageValidateCodeGenerator;
import com.caojx.learn.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.caojx.learn.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置类
 *
 * @author caojx
 * @version $Id: ValidateCodeBeanConfig.java,v 1.0 2020/2/22 6:39 下午 caojx
 * @date 2020/2/22 6:39 下午
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 注意 ImageCodeGenerator中没有@Service注解，
     * 可以使用 @ConditionalOnMissingBean 判断在没有imageCodeGenerator的情况下才初始化该bean
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
    public ValidateCodeGenerator imageValidateCodeGenerator() {
        ImageValidateCodeGenerator imageValidateCodeGenerator = new ImageValidateCodeGenerator();
        imageValidateCodeGenerator.setSecurityProperties(securityProperties);
        return imageValidateCodeGenerator;
    }

    /**
     * 支持切换不同的短信通过，通过实现SmsCodeSender接口
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }
}