package com.caojx.learn.security.core.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 自定义短信验证码SmsCodeAuthenticationProvider
 *
 * @author caojx
 * @version $Id: SmsCodeAuthenticationProvider.java,v 1.0 2020/2/23 2:13 下午 caojx
 * @date 2020/2/23 2:13 下午
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        String mobile = authenticationToken.getPrincipal().toString();
        UserDetails user = userDetailsService.loadUserByUsername(mobile);

        if(user == null){
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        // 查询用户信息成功，使用两个参数的构造器
        SmsCodeAuthenticationToken authenticationTokenResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());
        authenticationTokenResult.setDetails(authenticationToken.getDetails()); // 设置已认证的用户信息

        return authenticationTokenResult;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}