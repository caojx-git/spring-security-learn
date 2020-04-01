package com.caojx.learn.security.authorize.rbac.service.impl;

import com.caojx.learn.security.authorize.rbac.domain.Admin;
import com.caojx.learn.security.authorize.rbac.service.RbacService;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * rbac服务实现
 *
 * @author caojx created on 2020/3/27 10:56 上午
 */
@Component("rbacService")
public class RbacServiceImpl implements RbacService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /*@Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        boolean hasPermission = false;

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {

            // 读取用户所拥有权限的所有URL（假设去数据库中读取了相关的信息放到set中）
            String username = ((UserDetails) principal).getUsername();
            Set<String> urls = new HashSet<>();

            // 拿当前请求URL，与用户所拥有权限的所有URL进行匹配，匹配到则表示用户有权限访问该URL
            for (String url : urls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    hasPermission = true;
                    break;
                }
            }
        }
        return hasPermission;
    }*/


    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();

        boolean hasPermission = false;

        // 这里的Admin是用户实体类，不是管理员的意思
        if (principal instanceof Admin) {
            //如果用户名是admin，就永远返回true
            if (StringUtils.equals(((Admin) principal).getUsername(), "admin")) {
                hasPermission = true;
            } else {
                // 读取用户所拥有权限的所有URL
                Set<String> urls = ((Admin) principal).getUrls();
                for (String url : urls) {
                    if (antPathMatcher.match(url, request.getRequestURI())) {
                        hasPermission = true;
                        break;
                    }
                }
            }
        }

        return hasPermission;
    }

}