package com.wujt.com.wujt.shiro.jwt;

import com.wujt.com.wujt.shiro.jwt.AccessInfo;
import com.wujt.com.wujt.shiro.jwt.JWTUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.stereotype.Component;


/**
 * JWT身份认证实现类
 * AuthenticationToken接口的实现默认是UsernamePasswordToken(这个是在前后端不分离的情况下使用)，而getPrincipal()方法返回的是帐号,getCredentials()
 * 返回的是密码，咱这类比下，getPrincipal()和getCredentials()咱们全部用token代替
 * @author: wjt
 */
@Component
@Data
@NoArgsConstructor
public class JwtAuthenticationToken implements AuthenticationToken {

    /**
     * 是否是refresh token
     */
    private boolean refresh;
    /**
     * jwt token
     */
    private String token;

    private AccessInfo accessInfo;

    public JwtAuthenticationToken(String token) {
        this.token = token;
        this.accessInfo = JWTUtil.getAccessInfo(token);
    }

    @Override
    public Object getPrincipal() {
        return accessInfo;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
