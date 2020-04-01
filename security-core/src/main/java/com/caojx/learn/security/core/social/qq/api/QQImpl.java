package com.caojx.learn.security.core.social.qq.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * 自定义Api获取用户信息的实现，继承默认的API实现AbstractOAuth2ApiBinding
 * <p>
 * 注意，该类不能声明@Service 与@Component 把他作为一个单实例化的组件，因为AbstractOAuth2ApiBinding中的accessToken是private final的，所以需要作为多实例的组件
 *
 * @author caojx
 * @version $Id: QQImpl.java,v 1.0 2020/2/29 6:02 下午 caojx
 * @date 2020/2/29 6:02 下午
 */
@Slf4j
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    /**
     * QQ获取openId信息接口文档说明：https://wiki.connect.qq.com/%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7openid_oauth2-0
     */
    public static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    /**
     * QQ获取用户信息接口文档说明：https://wiki.connect.qq.com/get_user_info
     */
    public static final String URL_GET_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    /**
     * 获取QQ的用户信息需要的通用参数，appId
     * 提示：AbstractOAuth2ApiBinding已存在accessToken参数
     */
    private String appId;

    /**
     * 获取QQ的用户信息需要的通用参数，openId
     */
    private String openId;

    /**
     * objectMapper后边改用采用注入的方式，不用每次的new一个
     * https://blog.csdn.net/zxc_user/article/details/79713586
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    public QQImpl(String accessToken, String appId) {
        // TokenStrategy.ACCESS_TOKEN_PARAMETER 指定查询QQ用户信息的时候，将accessToken放到URL后边，默认是放在请求头上
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);

        this.appId = appId;

        // 获取openId
        String url = String.format(URL_GET_OPENID, accessToken);
        String result = getRestTemplate().getForObject(url, String.class);

        log.info(result);
        // {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"}
        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");

    }

    @Override
    public QQUserInfo getUserInfo() {
        try {
            // 获取用户信息
            String url = String.format(URL_GET_USER_INFO, appId, openId);
            String result = getRestTemplate().getForObject(url, String.class);
            log.info(result);

            //反序列化的时候如果多了其他属性,不抛出异常
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            QQUserInfo userInfo = objectMapper.readValue(result, QQUserInfo.class);

            userInfo.setOpenId(openId);
            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
}