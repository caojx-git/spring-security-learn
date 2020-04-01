package com.caojx.learn.security.core.suport;

import lombok.Data;

/**
 * 类注释，描述
 *
 * @author caojx
 * @version $Id: SocialUserInfo.java,v 1.0 2020/3/4 6:05 下午 caojx
 * @date 2020/3/4 6:05 下午
 */
@Data
public class SocialUserInfo {

    /**
     * 服务提供商 providerId
     */
    private String providerId;

    /**
     * 服务提供商 providerUserId，即openId
     */
    private String providerUserId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String headimg;

}