package com.caojx.learn.security.core.social.qq.connect;

import com.caojx.learn.security.core.social.qq.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;


/**
 * 使用ServiceProvider+ApiAdapter构建出ConnectionFactory
 * <p>
 * Connection他是由一个叫ConnectionFactory的连接工厂创建出来的，那么我们实际上用到的类叫做OAuth2ConnectionFactory，
 * 这个工厂负责创建我们的这个Connection实例，也就是包含了用户信息的这样一个对象
 *
 * @author caojx
 * @version $Id: QQConnectionFactory.java,v 1.0 2020/2/29 8:27 下午 caojx
 * @date 2020/2/29 8:27 下午
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        /**
         * 参数1：providerId 服务商唯一标识
         * 参数2：serviceProvider
         * 参数3：apiAdapter
         */
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }
}