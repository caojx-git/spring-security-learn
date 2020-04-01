package com.caojx.learn.security.core.social.qq.connect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * 自定义OAuth2Template的实现，添加能支持【text/html】的响应的MessageConvert
 *
 * @author caojx
 * @version $Id: QQOAuth2Template.java,v 1.0 2020/3/3 11:24 下午 caojx
 * @date 2020/3/3 11:24 下午
 */
@Slf4j
public class QQOAuth2Template extends OAuth2Template {

    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);

        // useParametersForClientAuthentication = true ,让OAuth2Template.exchangeForAccess 方法带上OAuth 协议一些默认的参数 client_id，client_secret
        setUseParametersForClientAuthentication(true);
    }

    /**
     * 处理 qq获取AccesToken的响应的特殊解析：access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14
     *
     * @param accessTokenUrl
     * @param parameters
     * @return
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String responseStr = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        log.info("获取accessToken的响应：" + responseStr);


        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr, "&");

        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");

        return new AccessGrant(accessToken, null, refreshToken, expiresIn);
    }


    /**
     * 添加能支持【text/html】的响应的MessageConvert
     *
     * @return
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }
}