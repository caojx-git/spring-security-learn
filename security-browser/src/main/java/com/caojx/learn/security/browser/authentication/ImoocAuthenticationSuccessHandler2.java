package com.caojx.learn.security.browser.authentication;


import com.caojx.learn.security.core.properties.SecurityProperties;
import com.caojx.learn.security.core.properties.SignInResponseType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义成功登录处理，继承AuthenticationSuccessHandler接口的实现类SavedRequestAwareAuthenticationSuccessHandler
 *
 * @author caojx
 * @version $Id: ImoocAuthenticationSuccessHandler2.java,v 1.0 2020/2/21 1:16 下午 caojx
 * @date 2020/2/21 1:16 下午
 */
@Component("imoocAuthenticationSuccessHandler")
public class ImoocAuthenticationSuccessHandler2 extends SavedRequestAwareAuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    private RequestCache requestCache = new HttpSessionRequestCache();

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
        if (SignInResponseType.JSON.equals(securityProperties.getBrowser().getSignInResponseType())) { //如果要返回JSON类型，则返回json格式，否则使用默认的处理方式
            // 这里将authentication响应给前端
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(authentication));
        } else {
            // 否则跳转页面
            // 如果设置了imooc.security.browser.singInSuccessUrl，总是跳到设置的地址上
            // 如果没设置，则尝试跳转到登录之前访问的地址上，如果登录前访问地址为空，则跳到网站根路径上
            if (StringUtils.isNotBlank(securityProperties.getBrowser().getSingInSuccessUrl())) {
                requestCache.removeRequest(httpServletRequest, httpServletResponse);
                setAlwaysUseDefaultTargetUrl(true);
                setDefaultTargetUrl(securityProperties.getBrowser().getSingInSuccessUrl());
            }
            super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        }

    }
}