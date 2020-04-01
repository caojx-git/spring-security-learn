package com.caojx.learn.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码处理器，封装不同的校验码处理逻辑
 *
 * @author caojx
 * @version $Id: ValidateCodeProcessor.java,v 1.0 2020/3/1 2:10 下午 caojx
 * @date 2020/3/1 2:10 下午
 */
public interface ValidateCodeProcessor {

    /**
     * 验证码放入session时的前缀
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    /**
     * 创建校验码
     *
     * @param request
     * @throws Exception
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * 校验验证码
     *
     * @param request
     * @throws Exception
     */
    void validate(ServletWebRequest request);

}