package com.caojx.learn.security.app;

import com.caojx.learn.security.core.properties.OAuth2ClientProperties;
import com.caojx.learn.security.core.properties.SecurityProperties;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义认证服务器
 *
 * @author caojx
 * @version $Id: ImoocAuthorizationServerConfig.java,v 1.0 2020/3/11 10:23 下午 caojx
 * @date 2020/3/11 10:23 下午
 */
@Configuration
@EnableAuthorizationServer
public class ImoocAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 继承了AuthorizationServerConfigurerAdapter类，就需要注入userDetailsService和authenticationManager
     * 如果没有继承，使用默认的机制不需要注入，Spring自己会去查找
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private TokenStore tokenStore;

    /**
     * required=false 可能不使用jwt
     */
    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    /**
     * 针对断点进行配置，这里就是配置org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)  // 指定tokenStore，这里我们使用redis存储token
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);

        // 如果jwtAccessTokenConverter 不为空，则使用JWT生成Token
        if(jwtAccessTokenConverter != null){
            endpoints.accessTokenConverter(jwtAccessTokenConverter);
        }

        // 使用jwtTokenEnhancer 增强token
        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
            // TokenEnhancerChain是增强器链
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(jwtTokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enhancers);

            endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
        }

    }

    /**
     * 跟客户端相关的配置，所谓的客户端就是有哪些应用来访问我们的系统
     * 配置了这个方法，下边两个方法就不生效了，会根据这个方法的配置决定给哪些应用发令牌
     * security.oauth2.client.client-id
     * security.oauth2.client.client-secret
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory() // 这里暂时将token放到内存中
//                .withClient("imooc") //client-id，withClient可以配置多个，对多个客户端进行配置 .and().withClient
//                .secret(passwordEncoder.encode("imooc-secret")) //client-secret，需要使用passwordEncoder对其进行加密
//                .accessTokenValiditySeconds(7200) // token有效时间
//                .authorizedGrantTypes("refresh_token", "password") // 支持的授权模式，字符串数组
//                .scopes("all", "read", "write"); // 令牌的权限有哪些，可以配置多个，这里配置了，客户端请求时就不用带scope参数了.如果客户端带了scope参数，则必须是配置的scope中的其中一个，否则会报错

        // 客户端信息在配置文件中配置
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if (ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
            for (OAuth2ClientProperties client : securityProperties.getOauth2().getClients()) {
                builder.withClient(client.getClientId())
                        .secret(passwordEncoder.encode(client.getClientSecret()))
                        .authorizedGrantTypes("refresh_token", "password") // 客户端支持的授权模式
                        .accessTokenValiditySeconds(client.getAccessTokenValidateSeconds()) // 令牌的有效时间，如果是0，永不过期
                        .refreshTokenValiditySeconds(2592000) // 配置refresh_token的有效时间，可以设置的比access_token长一点
                        .scopes("all", "read", "write"); // 令牌的权限有哪些，可以配置多个，这里配置了，客户端请求时就不用带scope参数了.如果客户端带了scope参数，则必须是配置的scope中的其中一个，否则会报错
            }
        }
    }

}