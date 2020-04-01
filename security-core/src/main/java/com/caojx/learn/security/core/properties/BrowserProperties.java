package com.caojx.learn.security.core.properties;

import lombok.Data;

@Data
public class BrowserProperties {

    /**
     * 注册页url，默认的注册页是 imooc-signUp.html
     */
    private String signUpUrl = "/imooc-signUp.html";

    /**
     * 登录页，默认是/imooc-signIn.html
     */
    private String loginPage = SecurityConstants.DEFAULT_LOGIN_PAGE_URL;

    /**
     * 登录成功与失败处理，默认返回JSON
     */
    private SignInResponseType signInResponseType = SignInResponseType.JSON;

    /**
     * 登录成功后跳转的地址，如果设置了此属性，则登录成功后总是会跳到这个地址上。
     * <p>
     * 只在signInResponseType为REDIRECT时生效
     */
    private String singInSuccessUrl;

    /**
     * 记住我的时间，默认3600秒
     */
    private int rememberMeSeconds = 3600;

    /**
     * session属性配置
     */
    private SessionProperties session = new SessionProperties();

    /**
     * 退出登录成功跳转的url，默认为空
     */
    private String signOutUrl;

}
