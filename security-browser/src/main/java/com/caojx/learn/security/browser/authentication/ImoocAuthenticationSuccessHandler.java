package com.caojx.learn.security.browser.authentication;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义成功登录处理，实现AuthenticationSuccessHandler接口
 *
 * @author caojx
 * @version $Id: ImoocAuthenticationSuccessHandler.java,v 1.0 2020/2/21 1:16 下午 caojx
 * @date 2020/2/21 1:16 下午
 */
//@Component("imoocAuthenticationSuccessHandler")
public class ImoocAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

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

        // 这里将authentication响应给前端
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(authentication));
    }
}