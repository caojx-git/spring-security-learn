package com.caojx.learn.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码生成接口
 *
 * @author caojx
 * @version $Id: ValidateCodeGenerator.java,v 1.0 2020/2/22 6:36 下午 caojx
 * @date 2020/2/22 6:36 下午
 */
public interface ValidateCodeGenerator {

    /**
     * 验证码生成接口（图形验证码、短信验证码）
     *
     * @param servletWebRequest
     * @return
     */
    ValidateCode generate(ServletWebRequest servletWebRequest);
}