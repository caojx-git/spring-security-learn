/**
 *
 */
package com.caojx.learn.security.core.social.weixin.connect;


import com.caojx.learn.security.core.social.weixin.api.Weixin;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * 微信连接工厂
 *
 * @author zhailiang
 *
 */
public class WeixinConnectionFactory extends OAuth2ConnectionFactory<Weixin> {

    /**
     * @param appId
     * @param appSecret
     */
    public WeixinConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new WeixinServiceProvider(appId, appSecret), new WeixinAdapter());
    }

    /**
     * 由于微信的openId是和accessToken一起返回的，所以在这里直接根据accessToken设置providerUserId即可，不用像QQ那样通过QQAdapter来获取
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if (accessGrant instanceof WeixinAccessGrant) {
            return ((WeixinAccessGrant) accessGrant).getOpenId();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.springframework.social.connect.support.OAuth2ConnectionFactory#createConnection(org.springframework.social.oauth2.AccessGrant)
     * 跟默认的实现对比，就是我在创建Connection的时候，前面传的这些参数都一样，不一样的是最后一个参数，在默认实现中这个getApiAdapter方法返回的这个apiAdapter每次都是同一个实例，也就是说OAuth2Connection拿到的都是同一个实例
     * 而在微信的这个实例中我是根据用户返回来的这个accessToken里边包含的微信的openId，针对每一个OAuth2Connection都new 了一个新的微信WeixinAdapter，这里是整个程序跟QQ最大的不一样的地方。就是WeixinAdapter是一个多实例的对象，
     * 而不是像做QQ的每次传回去的都是同一个实例，因为不同的人他的这个openId不一样，所以我这个WeixinAdapter.openId这个值就不一样，不一样的WeixinAdapter那那么它调出来的用户的信息就是不一样的，因为他openId不一样
     */
    @Override
    public Connection<Weixin> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<Weixin>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
    }

    /* (non-Javadoc)
     * @see org.springframework.social.connect.support.OAuth2ConnectionFactory#createConnection(org.springframework.social.connect.ConnectionData)
     */
    @Override
    public Connection<Weixin> createConnection(ConnectionData data) {
        return new OAuth2Connection<Weixin>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));
    }

    private ApiAdapter<Weixin> getApiAdapter(String providerUserId) {
        return new WeixinAdapter(providerUserId);
    }

    private OAuth2ServiceProvider<Weixin> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<Weixin>) getServiceProvider();
    }


}
