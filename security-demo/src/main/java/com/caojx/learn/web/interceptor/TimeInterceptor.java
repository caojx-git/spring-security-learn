package com.caojx.learn.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 方式2：使用Interceptor 记录Restful API接口服务时间
 *
 * 注意：Interceptor与Filter不一样，光声明@Component注解并不能让拦截器生效，
 * 还需要再配置类（com.caojx.learn.web.config.WebConfig）中进行配置
 *
 * @author caojx
 * @version $Id: TimeInterceptor.java,v 1.0 2020/2/18 3:37 下午 caojx
 * @date 2020/2/18 3:37 下午
 */
//@Component
public class TimeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle");

        System.out.println(((HandlerMethod)handler).getMethod().getName());
        request.setAttribute("startTime", new Date().getTime());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");

        long start = (Long)request.getAttribute("startTime");
        System.out.println("time interceptor 耗时："+(new Date().getTime()-start));
    }

    /**
     * 该方法不管成功或失败都会执行
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion");
        long start = (Long)request.getAttribute("startTime");
        System.out.println("time interceptor 耗时："+(new Date().getTime()-start));
        System.out.println("ex is:"+ex);
    }
}