package com.caojx.learn.sso.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端应用1
 *
 * @author caojx created on 2020/3/22 11:33 上午
 */
@RestController
@EnableOAuth2Sso // 开启SSO，表明自己是客户端
@SpringBootApplication
public class SsoClient1Application {

    public static void main(String[] args) {
        SpringApplication.run(SsoClient1Application.class, args);
    }

    /**
     * 获取登录后的用户信息
     *
     * @param user
     * @return
     */
    @GetMapping("/user")
    public Authentication user(Authentication user) {
        return user;
    }
}