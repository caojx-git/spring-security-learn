package com.caojx.learn.security.core.validate.code;

import com.caojx.learn.security.core.properties.SecurityProperties;
import com.caojx.learn.security.core.validate.code.image.ImageCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义短信验证码过滤器
 *
 * @author caojx
 * @version $Id: SmsValidateCodeFilter.java,v 1.0 2020/2/21 11:02 下午 caojx
 * @date 2020/2/21 11:02 下午
 */
//@Component("smsValidateCodeFilter")
public class SmsValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    private AuthenticationFailureHandler authenticationFailureHandler;

    private Set<String> urls = new HashSet<>();

    @Autowired
    private SecurityProperties securityProperties;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 初始化需要进行短信验证码校验的接口
     *
     * @throws ServletException
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        if(StringUtils.isNotBlank(securityProperties.getCode().getImage().getUrl())){
            String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getSms().getUrl(), ",");
            for (String url : configUrls) {
                urls.add(url);
            }
        }
        urls.add("/authentication/mobile"); // 登录的请求一定要验证码
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        boolean action = false;
        for (String url : urls) {
            if (pathMatcher.match(url, httpServletRequest.getRequestURI())) { // 使用Spring pathMatcher来判断是否匹配url，匹配则需要做验证码校验
                action = true;
                break;
            }
        }

        if (action) {
            try {
                validate(new ServletWebRequest(httpServletRequest));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        // 不是登录认证请求，直接放行，过滤器往后走
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    /**
     * 验证图形验证码是否正确
     *
     * @param servletWebRequest
     * @throws ServletRequestBindingException
     */
    private void validate(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
        // 获取缓存中的短信验证码
        ValidateCode codeInSession = (ValidateCode) sessionStrategy.getAttribute(servletWebRequest, ValidateCodeController.SESSION_SMS_KEY);
        // 获取表单中用户输入的短信验证码的值
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "smsCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpried()) {
            // 从session中移除验证码
            sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_SMS_KEY);
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        // 从session中移除验证码
        sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_SMS_KEY);
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}