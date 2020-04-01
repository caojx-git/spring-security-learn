package com.caojx.learn.security.core.social.weixin.connect;

import com.caojx.learn.security.core.social.weixin.api.Weixin;
import com.caojx.learn.security.core.social.weixin.api.WeixinUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 微信 api适配器，将微信 api的数据模型转为spring social的标准模型。
 *
 * @author caojx
 * @version $Id: WeixinAdapter.java,v 1.0 2020/3/6 4:41 下午 caojx
 * @date 2020/3/6 4:41 下午
 */
public class WeixinAdapter implements ApiAdapter<Weixin> {

    private String openId;

    public WeixinAdapter() {
    }

    public WeixinAdapter(String openId) {
        this.openId = openId;
    }

    /**
     * @param api
     * @return
     */
    @Override
    public boolean test(Weixin api) {
        return true;
    }

    /**
     * @param api
     * @param values
     */
    @Override
    public void setConnectionValues(Weixin api, ConnectionValues values) {
        WeixinUserInfo weixinUserInfo = api.getUserInfo(openId);
        values.setProviderUserId(weixinUserInfo.getOpenid());
        values.setDisplayName(weixinUserInfo.getNickname());
        values.setImageUrl(weixinUserInfo.getHeadimgurl());
    }

    /**
     * @param api
     * @return
     */
    @Override
    public UserProfile fetchUserProfile(Weixin api) {
        return null;
    }

    /**
     * @param api
     * @param message
     */
    @Override
    public void updateStatus(Weixin api, String message) {
        //do nothing
    }

}