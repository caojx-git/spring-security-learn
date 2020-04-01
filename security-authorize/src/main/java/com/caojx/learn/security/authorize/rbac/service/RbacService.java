package com.caojx.learn.security.authorize.rbac.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * rbac服务
 *
 * @author caojx created on 2020/3/27 10:54 上午
 */
public interface RbacService {

    /**
     * 使用自定义的方法，来告诉spring security是否有权限访问
     *
     * @param request        当前请求
     * @param authentication 用户认证信息
     * @return true 有权访问 false 无权访问
     */
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}