package com.caojx.learn.security.app.exception;

/**
 * 自定义APP安全异常类
 *
 * @author caojx created on 2020/3/19 9:49 下午
 */
public class AppSecretException extends RuntimeException {

    private static final long serialVersionUID = -1629364510827838114L;

    public AppSecretException(String msg) {
        super(msg);
    }
}