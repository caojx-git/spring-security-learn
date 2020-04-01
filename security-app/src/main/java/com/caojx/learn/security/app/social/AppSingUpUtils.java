package com.caojx.learn.security.app.social;

import com.caojx.learn.security.app.exception.AppSecretException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.TimeUnit;

/**
 * APP用户登录注册工具类，作用类似于ProviderSignInUtils（基于session），只不过AppSingUpUtils是基于redis的
 *
 * @author caojx created on 2020/3/19 9:47 下午
 */
@Component("appSingUpUtils")
public class AppSingUpUtils {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    /**
     * 缓存社交网站用户信息到redis，默认保存10分钟
     *
     * @param request
     * @param connectionData
     */
    public void saveConnectionData(WebRequest request, ConnectionData connectionData) {
        redisTemplate.opsForValue().set(getKey(request), connectionData, 10, TimeUnit.MINUTES);
    }

    /**
     * 将缓存的社交网站用户信息与系统注册用户信息绑定
     *
     * @param userId
     * @param request
     */
    public void doPostSignUp(String userId, WebRequest request) {

        String key = getKey(request);

        if (!redisTemplate.hasKey(key)) {
            throw new AppSecretException("无法找到缓存的用户社交账号信息");
        }

        // 获取redis中存储的社交用户信息
        ConnectionData connectionData = (ConnectionData) redisTemplate.opsForValue().get(key);

        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId())
                .createConnection(connectionData);

        // 将业务系统的用户的userId与社交用户的信息绑定
        usersConnectionRepository.createConnectionRepository(userId).addConnection(connection);

        // 绑定成功，清理redis key
        redisTemplate.delete(key);
    }

    /**
     * 获取redis key
     *
     * @param request
     * @return
     */
    private String getKey(WebRequest request) {
        // 获取设备id
        String deviceId = request.getHeader("deviceId");

        if (StringUtils.isBlank(deviceId)) {
            throw new AppSecretException("设备id参数不能为空");
        }
        return "imooc:security:social.connect." + deviceId;
    }
}