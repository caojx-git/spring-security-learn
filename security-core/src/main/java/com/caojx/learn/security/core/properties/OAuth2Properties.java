package com.caojx.learn.security.core.properties;

import lombok.Data;

/**
 * 支持多个客户端配置
 *
 * @author caojx created on 2020/3/21 4:20 下午
 */
@Data
public class OAuth2Properties {

    /**
     * 支持多个客户端配置
     */
    private OAuth2ClientProperties[] clients = {};

    /**
     * 使用jwt时为token签名的秘钥，默认imooc
     */
    private String jwtSigningKey = "imooc";
}