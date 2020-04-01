package com.caojx.learn.security.app.authentication;


import com.caojx.learn.security.core.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/**
 * 自定义成功登录处理，继承AuthenticationSuccessHandler接口的实现类SavedRequestAwareAuthenticationSuccessHandler
 *
 * @author caojx
 * @version $Id: ImoocAuthenticationSuccessHandler.java,v 1.0 2020/2/21 1:16 下午 caojx
 * @date 2020/2/21 1:16 下午
 */
@Component("imoocAuthenticationSuccessHandler")
public class ImoocAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    /**
     * 自定义成功登录处理
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication      该对象封装了认证授权信息
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功");

        // 从请求头中获取clientId、clientSecret
        String header = httpServletRequest.getHeader("Authorization");
        if (header == null && !header.toLowerCase().startsWith("basic ")) {
            throw new UnapprovedClientAuthenticationException("请求头中无client信息");
        }
        String[] tokens = this.extractAndDecodeHeader(header, httpServletRequest);

        assert tokens.length == 2;

        String clientId = tokens[0];
        String clientSecret = tokens[1];

        // 获取clientDetails
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
        } else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
            //传入的clientSecret与配置的clientSecret不匹配
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientId);
        }

        /**
         * 创建TokenRequest
         *
         * 第一个参数：由于这里用户已经登录成功了，不需要再去构建authentication，所以TokenRequest构造器的第一个参数，传一个空的Map
         * 第二个参数：clientId
         * 第三个参数：clientDetails.getScope(),就是这个应该配置了什么权限我就直接给什么权限，不再像spring security oauth标准逻辑一样再去做一些你请求的这个权限是不是已经给你配置了，就不再做这些scope相关的校验了，
         * 因为这是在我们自己的app和前端之间做这个事，所以我直接就把所有权限都给你
         * 第四个参数：在标准的授权模式中，这就是4中授权模式类型，这里我们写custom表示自定义的。
         */
        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");

        // 创建OAuth2Request
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

        // 创建OAuth2Authentication
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        // 创建OAuth2AccessToken
        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

        // 这里将OAuth2AccessToken响应给前端
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(token));

    }

    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {
        byte[] base64Token = header.substring(6).getBytes("UTF-8");

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var7) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, "UTF-8");
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        } else {
            return new String[]{token.substring(0, delim), token.substring(delim + 1)};
        }
    }
}