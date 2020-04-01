package com.caojx.learn.security.core.social;

import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

/**
 * Springboot autoconfigure 中2.x 不存在SocialAutoConfigurerAdapter、SocialProperties和SocialWebAutoConfiguration，直接将springboot autoconfigure 1.5.x中这三个类拷贝到我们项目里
 *
 * @author caojx
 * @version $Id: SocialAutoConfigurerAdapter.java,v 1.0 2020/2/29 9:36 下午 caojx
 * @date 2020/2/29 9:36 下午
 */
public abstract class SocialAutoConfigurerAdapter extends SocialConfigurerAdapter {
    public SocialAutoConfigurerAdapter() {
    }

    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(this.createConnectionFactory());
    }

    protected abstract ConnectionFactory<?> createConnectionFactory();
}