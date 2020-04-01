package com.caojx.learn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 类注释，描述
 *
 * @author caojx created on 2020/3/27 4:04 下午
 */
@RestController
@SpringBootApplication
public class TestApplication {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @GetMapping("/me")
    public Authentication me(Authentication authentication) {
        return authentication;
    }

    @PostMapping("/user/regist")
    public void regist(HttpServletRequest request){
        providerSignInUtils.doPostSignUp("jojo", new ServletWebRequest(request));
    }
}