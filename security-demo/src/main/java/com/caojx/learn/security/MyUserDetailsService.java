package com.caojx.learn.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

/**
 * 实现Spring Security的 UserDetailsService，实现自定义的用户读取逻辑
 * <p>
 * 注意：这里为了演示节约时间，并不会真的去连接数据库，到了这里相信大家都知道怎么从数据库中读取用户了
 *
 * @author caojx
 * @version $Id: MyUserDetailsService.java,v 1.0 2020/2/20 3:03 下午 caojx
 * @date 2020/2/20 3:03 下午
 */
@Service
public class MyUserDetailsService implements UserDetailsService, SocialUserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 表单登录，查找用户信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("登录用户名：" + username);
        return buildUser(username);
    }

    /**
     * 这个接口中的方法叫SocialUserDetails loadUserByUserId(String userId) ，
     * 这个方法是在社交登录的时候用的，传进来的是Spring Social 根据社交网站的用户的openId（providerUserId），
     * 查出来的用户的userId。你要做的就是根据这个userId去构建出了一个SocialUserDetails（UserDetails的子类）的实现。
     *
     * @param userId
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        logger.info("社交用户名：" + userId);
        return buildUser(userId);
    }

    private SocialUserDetails buildUser(String userId) {
        // 根据用户名查找到用户信息 --可以去数据库中查询

        // 由于UserDetails是一个接口，我们使用Spring Security提供的UserDetails的实现类User，当然也可以自定义实现UserDetails的类
        // 用户名、密码、拥有的权限集合  这里暂时都写死一些值，构造一个写死的用户返回
        //return new User(username, "123456", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

        // 根据查找到的用户信息判断用户是否被冻结，这里返回一个账户锁定的用户
//        return new User(username, "123456", true, true, true, false,
//                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

        // 注意这个密码是数据库加密后的密码
        String password = passwordEncoder.encode("123456");
        logger.info("数据库密码是：" + password);
        return new SocialUser(userId, password, true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("xxx"));
    }
}