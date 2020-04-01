package com.caojx.learn.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码存储器接口
 *
 * @author caojx
 * @version $Id: ValidateCodeRepository.java,v 1.0 2020/3/15 8:55 下午 caojx
 * @date 2020/3/15 8:55 下午
 */
public interface ValidateCodeRepository {

    /**
     * 保存验证码
     *
     * @param request
     * @param code
     * @param validateCodeType
     */
    void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType);

    /**
     * 获取验证码
     *
     * @param request
     * @param validateCodeType
     * @return
     */
    ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType);

    /**
     * 移除验证码
     *
     * @param request
     * @param codeType
     */
    void remove(ServletWebRequest request, ValidateCodeType codeType);
}