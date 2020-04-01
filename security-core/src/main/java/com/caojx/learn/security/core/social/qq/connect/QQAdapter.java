package com.caojx.learn.security.core.social.qq.connect;

import com.caojx.learn.security.core.social.qq.api.QQ;
import com.caojx.learn.security.core.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 自定义ApiAdapter
 * 它的作用就是在我们自己写的Api所获取到的个性化的服务提供商的用户数据和Spring Social标准数据结构之间做一个适配
 *
 * @author caojx
 * @version $Id: QQAdapter.java,v 1.0 2020/2/29 8:15 下午 caojx
 * @date 2020/2/29 8:15 下午
 */
public class QQAdapter implements ApiAdapter<QQ> {

    /**
     * QQ API是不是通的，返回true
     *
     * @param api
     * @return
     */
    @Override
    public boolean test(QQ api) {
        return true;
    }

    /**
     * 映射成 标准的信息结构
     *
     * @param api
     * @param connectionValues
     */
    @Override
    public void setConnectionValues(QQ api, ConnectionValues connectionValues) {
        // 获取用户信息
        QQUserInfo userInfo = api.getUserInfo();

        // 昵称
        connectionValues.setDisplayName(userInfo.getNickname());
        // 头像url
        connectionValues.setImageUrl(userInfo.getFigureurl_qq_1());
        // 个人主页
        connectionValues.setProfileUrl(null);
        // 用户openId
        connectionValues.setProviderUserId(userInfo.getOpenId());

    }

    @Override
    public UserProfile fetchUserProfile(QQ qq) {
        return null;
    }

    @Override
    public void updateStatus(QQ api, String message) {
        // do noting
    }
}