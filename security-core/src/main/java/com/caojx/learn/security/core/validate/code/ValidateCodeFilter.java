package com.caojx.learn.security.core.validate.code;

import com.caojx.learn.security.core.properties.SecurityConstants;
import com.caojx.learn.security.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 自定义验证码过滤器
 *
 * @author caojx
 * @version $Id: ValidateCodeFilter.java,v 1.0 2020/2/21 11:02 下午 caojx
 * @date 2020/2/21 11:02 下午
 */
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    /**
     * 验证码校验失败处理器
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 系统配置信息
     */
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 系统中的校验码处理器
     */
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 存放所有需要校验验证码的url
     */
    private Map<String, ValidateCodeType> urlMap = new HashMap<>();

    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 初始化需要进行图形验证码校验的接口
     *
     * @throws ServletException
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
        addUrlToMap(securityProperties.getCode().getImage().getUrl(), ValidateCodeType.IMAGE);

        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
        addUrlToMap(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.SMS);
    }

    /**
     * 将系统中配置的需要校验验证码的URL根据校验的类型放入map
     *
     * @param urlString
     * @param type
     */
    protected void addUrlToMap(String urlString, ValidateCodeType type) {
        if (StringUtils.isNotBlank(urlString)) {
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
            for (String url : urls) {
                urlMap.put(url, type);
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 验证码类型
        ValidateCodeType type = getValidateCodeType(request);
        if (type != null) {
            logger.info("校验请求（" + request.getRequestURL() + ")中的验证码，验证码类型" + type);
            try {
                // 校验验证码
                validateCodeProcessorHolder.findValidateCodeProcessor(type)
                        .validate(new ServletWebRequest(request, response));
                logger.info("验证码校验通过");
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        // 不是验证码请求类型，直接放行
        filterChain.doFilter(request, response);
    }

    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType result = null;
        Set<String> urls = urlMap.keySet();
        for (String url : urls) {
            if (pathMatcher.match(url, request.getRequestURI())) {
                result = urlMap.get(url);
                break;
            }
        }
        return result;
    }

//
//    /*
//
//       /**
//     * 初始化需要进行图形验证码校验的接口
//     *
//     * @throws ServletException
//     */
//    @Override
//    public void afterPropertiesSet() throws ServletException {
//        super.afterPropertiesSet();
//        if(StringUtils.isNotBlank(securityProperties.getCode().getImage().getUrl())){
//            String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getImage().getUrl(), ",");
//            for (String url : configUrls) {
//                urls.add(url);
//            }
//        }
//        urls.add("/authentication/form"); // 登录的请求一定要验证码
//    }
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        boolean action = false;
//        for (String url : urls) {
//            if (pathMatcher.match(url, httpServletRequest.getRequestURI())) { // 使用Spring pathMatcher来判断是否匹配url，匹配则需要做验证码校验
//                action = true;
//                break;
//            }
//        }
//
//        if (action) {
//            try {
//                validate(new ServletWebRequest(httpServletRequest));
//            } catch (ValidateCodeException e) {
//                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
//                return;
//            }
//        }
//        // 不是登录认证请求，直接放行，过滤器往后走
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }

/*
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 登录请求
        if (StringUtils.endsWithIgnoreCase("/authentication/form", httpServletRequest.getRequestURI())
                && StringUtils.endsWithIgnoreCase(httpServletRequest.getMethod(), "post")) {
            try {
                validate(new ServletWebRequest(httpServletRequest));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }

        // 不是登录认证请求，直接放行，过滤器往后走
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }*/

    /*
     *//**
     * 验证图形验证码是否正确
     *
     * @param servletWebRequest
     * @throws ServletRequestBindingException
     *//*
    private void validate(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
        // 获取缓存中的图形验证码
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(servletWebRequest, ValidateCodeController.SESSION_IMAGE_KEY);
        // 获取表单中用户输入的图形验证码的值
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpried()) {
            // 从session中移除验证码
            sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_IMAGE_KEY);
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        // 从session中移除验证码
        sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_IMAGE_KEY);
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }*/
}