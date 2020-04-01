package com.caojx.learn.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 验证码处理器辅助类
 *
 * @author caojx
 * @version $Id: ValidateCodeProcessorHolder.java,v 1.0 2020/3/1 2:49 下午 caojx
 * @date 2020/3/1 2:49 下午
 */
@Component
public class ValidateCodeProcessorHolder {

    /**
     * Spring会默认收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
     */
    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    /**
     * 根据验证码类型获取验证码处理器
     *
     * @param type
     * @return
     */
    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
        return findValidateCodeProcessor(type.toString().toLowerCase());
    }

    public ValidateCodeProcessor findValidateCodeProcessor(String type) {
        // 获取验证码处理器的名字, 比如sms + ValidateCodeProcessor = smsValidateCodeProcessor
        String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
        ValidateCodeProcessor processor = validateCodeProcessors.get(name);
        if (processor == null) {
            throw new ValidateCodeException("验证码处理器" + name + "不存在");
        }
        return processor;
    }

}