package com.gow.security.privisioning;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author gow
 * @date 2021/8/2 0002
 */
public interface MutableUserDetails extends UserDetails {

    void setPassword(String password);

    String getUserId();
}