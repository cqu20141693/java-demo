package com.gow.security.privisioning;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gow.security.jwt.JWTUser;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author gow
 * @date 2021/8/2 0002
 */
public class MutableUser implements MutableUserDetails {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String password;

    private JWTUser delegate;

    public MutableUser() {
    }

    public MutableUser(JWTUser user) {
        this.delegate = user;
        this.password = user.getPassword();
    }

    public JWTUser getDelegate() {
        return delegate;
    }

    public void setDelegate(JWTUser delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUserId() {
        return delegate.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.delegate.getAuthorities();
    }

    @Override
    public String getUsername() {
        return this.delegate.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.delegate.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.delegate.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.delegate.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.delegate.isEnabled();
    }
}
