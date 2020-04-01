package com.caojx.learn.security.core.validate.code.impl;

import com.caojx.learn.security.core.validate.code.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * 抽象的 ValidateCodeProcessor 验证码处理器默认实现
 *
 * @author caojx
 * @version $Id: AbstractValidateCodeProcessor.java,v 1.0 2020/3/1 2:13 下午 caojx
 * @date 2020/3/1 2:13 下午
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 验证码存储器
     */
    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    /**
     * Spring会默认收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
     */
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;

    @Override
    public void create(ServletWebRequest request) throws Exception {
        // 生成验证码
        C validateCode = generate(request);
        // 保存验证码到session中
        save(request, validateCode);
        // 发送验证码
        send(request, validateCode);
    }

    private C generate(ServletWebRequest request) {
        String type = getValidateCodeType(request).toString().toLowerCase();
        String generatorName = type + ValidateCodeGenerator.class.getSimpleName();
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
        if (validateCodeGenerator == null) {
            throw new ValidateCodeException("验证码生成器" + generatorName + "不存在");
        }
        return (C) validateCodeGenerator.generate(request);
    }

    /**
     * 保存验证码
     *
     * @param request
     * @param validateCode
     */
    private void save(ServletWebRequest request, C validateCode) {
        // 如果是图形验证码，我们只存验证码，和过期时间到session中，不直接把图形验证码中的BufferedImage放到session中，因为如果spring-session存储类型选比如说是redis的话，需要将存进去的对象做序列化，BufferedImage没有实现Serializable，不好序列化
        ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
//        sessionStrategy.setAttribute(request, getSessionKey(request), code);

        // 使用外部存储器存储验证码
        validateCodeRepository.save(request, code, getValidateCodeType(request));
    }

    /**
     * 获取session 时的key
     *
     * @param request
     * @return
     */
    private String getSessionKey(ServletWebRequest request) {
        return ValidateCodeProcessor.SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
    }

    /**
     * 发送校验码，由子类实现
     *
     * @param request
     * @param validateCode
     * @throws Exception
     */
    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

    /**
     * 根据请求的url获取校验码的类型
     * <p>
     * 注意：getClass()获取 AbstractValidateCodeProcessor 的具体的实现类
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "ValidateCodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }


    @Override
    public void validate(ServletWebRequest request) {
        ValidateCodeType codeType = getValidateCodeType(request);

//        String sessionKey = getSessionKey(request);
//        C codeInSession = (C) sessionStrategy.getAttribute(request, sessionKey);

        // 从外部存储中获取验证码
        C codeInSession = (C) validateCodeRepository.get(request, codeType);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), codeType.getParamNameOnValidate());
        } catch (Exception e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(codeType + "验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException(codeType + "验证码不存在");
        }

        if (codeInSession.isExpried()) {
//            sessionStrategy.removeAttribute(request, sessionKey);
            validateCodeRepository.remove(request, codeType);
            throw new ValidateCodeException(codeType + "验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException(codeType + "验证码不匹配");
        }

        validateCodeRepository.remove(request, codeType);
//        sessionStrategy.removeAttribute(request, sessionKey);
    }
}