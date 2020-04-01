package com.caojx.learn.security.core.social.weixin.connect;

import org.springframework.social.oauth2.AccessGrant;

/**
 * 微信的access_token信息。与标准OAuth2协议不同，微信在获取access_token时会同时返回openId,并没有单独的通过accessToken换取openId的服务
 * 所以在这里继承了标准AccessGrant，添加了openId字段，作为对微信access_token信息的封装。
 *
 * 我们声明了一个叫微信WeixinAccessGrant的这样一个类，因为标准的OAuth协议返回的数据中是不包含openId，
 * Spring Social提供的这个对令牌的封装AccessGrant的类里面是没有openId，那么我需要自个定义一个类WeixinAccessGrant去继承它，
 * 然后新声明了一个openId字段来存微信走完OAuth流程以后多返回来的那个openId字段。
 *
 * @author caojx
 * @version $Id: WeixinAccessGrant.java,v 1.0 2020/3/6 2:30 下午 caojx
 * @date 2020/3/6 2:30 下午
 */
public class WeixinAccessGrant  extends AccessGrant {

    private String openId;

    public WeixinAccessGrant() {
        super("");
    }

    public WeixinAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn) {
        super(accessToken, scope, refreshToken, expiresIn);
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}