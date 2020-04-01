package com.caojx.learn.service.impl;

import com.caojx.learn.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * 类注释，描述
 *
 * @author caojx
 * @version $Id: HelloServiceImpl.java,v 1.0 2020/2/17 9:38 下午 caojx
 * @date 2020/2/17 9:38 下午
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String greeting(String name) {
        System.out.println("greeting");
        return "hello "+name;
    }
}