package com.caojx.learn.sso.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * 认证服务器配置
 *
 * @author caojx created on 2020/3/22 10:56 上午
 */
@Configuration
@EnableAuthorizationServer // 有了这个注解，当前应用就是一个标准的OAuth2认证服务器
public class SsoAuthorizationServiceConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore jwtTokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;


    /**
     * 配置认证服务器需要给哪些应用发令牌，我们的例子中是imooc1和imooc2
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("imooc1") // clientId
                    .secret(passwordEncoder.encode("imooc-secret1")) // clientSecret，springboot2.需要对secret进行encode
                    .authorizedGrantTypes("authorization_code", "refresh_token") // 支持的授权模式
                    .redirectUris("http://127.0.0.1:8080/client1/login") // 授权成功后，回调地址
                    .scopes("all")
                    .and()
                .withClient("imooc2")
                    .secret(passwordEncoder.encode("imooc-secret2"))
                    .authorizedGrantTypes("authorization_code", "refresh_token")
                    .redirectUris("http://127.0.0.1:8060/client2/login")
                    .scopes("all");
    }

    /**
     * 配置使用JWT生产令牌
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.tokenStore(jwtTokenStore).accessTokenConverter(jwtAccessTokenConverter).userDetailsService(userDetailsService);
        endpoints.tokenStore(jwtTokenStore).accessTokenConverter(jwtAccessTokenConverter);
    }


    /**
     * 认证服务器的安全配置，配置的 isAuthenticated() 是SpringSecurity的授权表达式，它的意思是访问认证服务器的tokenKey的时候需要进行认证，
     * 默认是 denyAll()，拒绝所有访问 {@link AuthorizationServerSecurityConfigurer#tokenKeyAccess}。
     * <p>
     * 这里tokenkey指的是对jwt进行签名秘钥 {@link SsoServerBeanConfig#jwtAccessTokenConverter()}，因为后边client应用拿到jwt token后，需要对jwt token进行解析，就需要用到tokenKey秘钥验证签名，
     * 对于客户端来说，客户端需要拿到tokenKey会配置对应的路径获取tokenKey
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
        security.tokenKeyAccess("isAuthenticated()");
    }

}