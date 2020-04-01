package com.caojx.learn.web.aspect;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//@Aspect
//@Component
public class TimeAspect {

    /**
     * 切入点表达式，可以参考官方文档
     * https://docs.spring.io/spring/docs/5.1.13.RELEASE/spring-framework-reference/core.html#aop
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
//    @Around("execution(* com.caojx.learn.web.controller.UserController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {

        System.out.println("time aspect start");

        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            System.out.println("arg is " + arg);
        }

        long start = new Date().getTime();

        Object object = pjp.proceed();

        System.out.println("time aspect 耗时:" + (new Date().getTime() - start));

        System.out.println("time aspect end");

        return object;
    }

}
