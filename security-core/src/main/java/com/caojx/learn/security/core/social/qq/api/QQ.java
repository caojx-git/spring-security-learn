package com.caojx.learn.security.core.social.qq.api;

/**
 * 自定义Api获取用户信息的接口，实现继承默认的AbstractOAuth2ApiBinding
 *
 * @author caojx
 * @version $Id: QQ.java,v 1.0 2020/2/29 6:00 下午 caojx
 * @date 2020/2/29 6:00 下午
 */
public interface QQ {

    /**
     * 获取QQ的用户信息
     * @return
     */
    QQUserInfo getUserInfo();
}