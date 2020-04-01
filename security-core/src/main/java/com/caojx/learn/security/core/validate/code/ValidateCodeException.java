package com.caojx.learn.security.core.validate.code;


import org.springframework.security.core.AuthenticationException;

/**
 * 自定义验证码异常
 *
 * @author caojx
 * @version $Id: ValidateCodeException.java,v 1.0 2020/2/21 11:06 下午 caojx
 * @date 2020/2/21 11:06 下午
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
}