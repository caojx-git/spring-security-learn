package com.caojx.learn.security.browser.authentication;

import com.caojx.learn.security.core.properties.SignInResponseType;
import com.caojx.learn.security.core.suport.SimpleResponse;
import com.caojx.learn.security.core.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义成功登录失败处理，继承AuthenticationFailureHandler接口的实现类SimpleUrlAuthenticationFailureHandler接口
 *
 * @author caojx
 * @version $Id: ImoocAuthenticationFailureHandler.java,v 1.0 2020/2/21 1:31 下午 caojx
 * @date 2020/2/21 1:31 下午
 */
@Component("imoocAuthenticationFailureHandler")
public class ImoocAuthenticationFailureHandler2 extends SimpleUrlAuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exception) throws IOException, ServletException {
        logger.info("登录失败");
        if (SignInResponseType.JSON.equals(securityProperties.getBrowser().getSignInResponseType())) { //如果要返回JSON类型，则返回json格式，否则使用默认的处理方式
            // 将异常以json格式响应回前端
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpServletResponse.setContentType("application/json;charset=UTF-8");
//            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(exception));
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));

        } else {
            super.onAuthenticationFailure(httpServletRequest, httpServletResponse, exception);
        }
    }
}