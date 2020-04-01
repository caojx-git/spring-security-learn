package com.caojx.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 类注释，描述
 *
 * @author caojx
 * @version $Id: DemoApplication.java,v 1.0 2020/2/16 10:07 下午 caojx
 * @date 2020/2/16 10:07 下午
 */
@EnableSwagger2
@RestController
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello spring security";
    }
}