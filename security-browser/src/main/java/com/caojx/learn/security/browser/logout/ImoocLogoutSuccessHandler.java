package com.caojx.learn.security.browser.logout;

import com.caojx.learn.security.core.suport.SimpleResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录成功处理器
 *
 * @author caojx
 * @version $Id: ImoocLogoutSuccessHandler.java,v 1.0 2020/3/9 2:44 下午 caojx
 * @date 2020/3/9 2:44 下午
 */
@Slf4j
public class ImoocLogoutSuccessHandler implements LogoutSuccessHandler {

    private String signOutUrl;

    private ObjectMapper objectMapper = new ObjectMapper();

    public ImoocLogoutSuccessHandler(String signOutUrl) {
        this.signOutUrl = signOutUrl;
    }

    /**
     * 退出登录成功处理器，如果用户配置了退出登录跳转页面，则退出成功后跳转到对应页面，如果没有配置，则响应一段JSON
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        log.info("退出登录成功");

        if (StringUtils.isBlank(signOutUrl)) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("退出成功")));
        } else {
            httpServletResponse.sendRedirect(signOutUrl);
        }
    }

}