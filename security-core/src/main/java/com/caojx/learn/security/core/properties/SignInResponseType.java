package com.caojx.learn.security.core.properties;

/**
 * 登录类型配置，决定在登录成功或失败时怎么响应给前端，响应json，还是跳页面
 *
 * @author caojx
 * @version $Id: SignInResponseType.java,v 1.0 2020/2/21 1:47 下午 caojx
 * @date 2020/2/21 1:47 下午
 */
public enum  SignInResponseType {

    REDIRECT,

    JSON,

    ;
}