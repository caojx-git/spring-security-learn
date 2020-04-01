package com.caojx.learn.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * ConnectionSignUp 接口实现
 *
 * @author caojx
 * @version $Id: DemoConnectionSignUp.java,v 1.0 2020/3/4 9:22 下午 caojx
 * @date 2020/3/4 9:22 下午
 */
//@Component
public class DemoConnectionSignUp implements ConnectionSignUp {

    @Override
    public String execute(Connection<?> connection) {
        // 加入根据社交用户信息，默认在数据库中创建了一个用户

        // 根据社交用户信息默认创建用户并返回用户唯一标识，这里我们假设用户名作为唯一标识
        return connection.getDisplayName();
    }
}