package com.caojx.learn.security.core.social.weixin.api;

/**
 * 微信API调用接口
 *
 * @author caojx
 * @version $Id: Weixin.java,v 1.0 2020/3/6 1:04 下午 caojx
 * @date 2020/3/6 1:04 下午
 */
public interface Weixin {

    /**
     * 获取微信用户信息
     * @param openId
     * @return
     */
    WeixinUserInfo getUserInfo(String openId);
}