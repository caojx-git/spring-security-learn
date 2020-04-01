package com.caojx.learn.web.filter;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

/**
 * 方式1：使用filter 记录Restful API接口服务时间
 *
 * 注意，上边的过滤器是我们自己编写的，可以在过滤器中添加@Component注解，让Spring可以自动识别我们的过滤器，假如对于一些第三方的过滤器是不一定有这个注解的，
 * 怎么让Spring来识别到呢？对于以前的非SpringBoot的web项目一般是配置在web.xml中，对于SpringBoot项目的话，一般编写配置类（com.caojx.learn.web.config.WebConfig），
 * 使用代码的方式来注册过滤器，配置类就相当于以前的配置文件
 *
 * @author caojx
 * @version $Id: TimeFilter.java,v 1.0 2020/2/18 2:56 下午 caojx
 * @date 2020/2/18 2:56 下午
 */
//@Component
public class TimeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("time filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("time filter start");
        long start = new Date().getTime();
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("time filter:" + (new Date().getTime() - start));
        System.out.println("time filter finish");
    }

    @Override
    public void destroy() {
        System.out.println("time filter destroy");
    }
}