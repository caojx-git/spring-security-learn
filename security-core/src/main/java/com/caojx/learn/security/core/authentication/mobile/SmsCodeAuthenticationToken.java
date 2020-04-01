package com.caojx.learn.security.core.authentication.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 参考复制UsernamePasswordAuthenticationToken的代码，写SmsCodeAuthenticationToken，去掉跟密码相关的内容
 *
 * @author caojx
 * @version $Id: SmsCodeAuthenticationToken.java,v 1.0 2020/2/23 2:01 下午 caojx
 * @date 2020/2/23 2:01 下午
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal; //手机号

    public SmsCodeAuthenticationToken(Object mobile) {
        super((Collection) null);
        this.principal = mobile;
        this.setAuthenticated(false);
    }

    public SmsCodeAuthenticationToken(Object mobile, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = mobile;
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}