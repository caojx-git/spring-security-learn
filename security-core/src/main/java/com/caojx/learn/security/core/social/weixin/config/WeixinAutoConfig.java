package com.caojx.learn.security.core.social.weixin.config;

import com.caojx.learn.security.core.properties.SecurityProperties;
import com.caojx.learn.security.core.properties.WeixinProperties;
import com.caojx.learn.security.core.social.ImoocConnectView;
import com.caojx.learn.security.core.social.SocialAutoConfigurerAdapter;
import com.caojx.learn.security.core.social.weixin.connect.WeixinConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.web.servlet.View;

/**
 * 微信登录配置
 *
 * @author caojx
 * @version $Id: WeixinAutoConfig.java,v 1.0 2020/3/6 5:23 下午 caojx
 * @date 2020/3/6 5:23 下午
 */
@Configuration
@ConditionalOnProperty(prefix = "imooc.security.social.weixin", name = "app-id")
public class WeixinAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter
     * #createConnectionFactory()
     */
    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        WeixinProperties weixinConfig = securityProperties.getSocial().getWeixin();
        return new WeixinConnectionFactory(weixinConfig.getProviderId(), weixinConfig.getAppId(),
                weixinConfig.getAppSecret());
    }

    /**
     * 配置默认的微信绑定成功的connect/weixinConnected视图，解除绑定的视图名为connect/weixinConnect，区别就在与有没有ed
     * 他们都将交给ImoocConnectView视图去处理，ImoocConnectView的model中有connection信息表示进行绑定，没有connection信息表示进行解绑
     *
     * @return
     */
    @Bean({"connect/weixinConnected", "connect/weixinConnect"})
    @ConditionalOnMissingBean(name = "weixinConnectedView")
    public View weixinConnectedView() {
        return new ImoocConnectView();
    }
}