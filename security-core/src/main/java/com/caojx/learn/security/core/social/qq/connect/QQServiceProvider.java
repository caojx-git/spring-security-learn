package com.caojx.learn.security.core.social.qq.connect;

import com.caojx.learn.security.core.social.qq.api.QQ;
import com.caojx.learn.security.core.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * 用默认的OAuth2Template实现+自定义Api的实现构建ServiceProvider（服务提供商）
 *
 * @author caojx
 * @version $Id: QQServiceProvider.java,v 1.0 2020/2/29 6:55 下午 caojx
 * @date 2020/2/29 6:55 下午
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;

    /**
     * 接口文档参考：https://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

    public static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    public QQServiceProvider(String appId, String appSecret) {
        /**
         *
         * OAuth2Operations（默认实现OAuth2Template），这个就是OAuth协议相关的一些操作，实际上，这个接口封装了从第一步到第五步这样一个标准的OAuth协议的处理流程
         *
         * 第1、2个参数：
         * 在QQ互联上注册的时候，QQ互联会给每一个应用分配 appId, appSecret，相当于APP的用户名密码，
         * 每次需要带着appId, appSecret,，QQ才能知道是哪个应用在向QQ发送请求
         *
         * 第3个参数：authorizeUrl 对应第1步，将用户导向认证服务器的url
         *
         * 第4个参数：accessTokenUrl 对应第4步，申请令牌的url
         */
//        super(new OAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, appId);
    }
}