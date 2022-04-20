package com.gow.security.jwt;

import java.util.Set;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author gow
 * @date 2021/8/3 0003
 */
@Data
public class TokenInfo {
    private JWTUser jwtUser;
    private Set<SimpleGrantedAuthority> authorities;
}
