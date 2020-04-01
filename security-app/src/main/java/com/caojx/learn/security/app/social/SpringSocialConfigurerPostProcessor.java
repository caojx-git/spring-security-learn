package com.caojx.learn.security.app.social;

import com.caojx.learn.security.core.social.ImoocSpringSocialConfigurer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 在App初始化完成之后修改掉signup的路径
 *
 * @author caojx created on 2020/3/19 10:00 下午
 */
@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

    /**
     * (non-Javadoc)
     * <p>
     * 在bean实例化之前会调用这个方法，这里什么都不做，原样返回
     *
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * (non-Javadoc)
     * <p>
     * 在imoocSocialSecurityConfig bean实例化之后修改其signUpUrl
     * 由于所有bean在实例化之后都会经过这个方法，所以要判断一下bean的名字
     *
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StringUtils.equals(beanName, "imoocSocialSecurityConfig")) {
            ImoocSpringSocialConfigurer config = (ImoocSpringSocialConfigurer) bean;
            config.signupUrl("/social/signUp");
            return config;
        }
        return bean;
    }
}