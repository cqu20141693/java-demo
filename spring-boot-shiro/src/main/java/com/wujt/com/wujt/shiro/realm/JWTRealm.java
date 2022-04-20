package com.wujt.com.wujt.shiro.realm;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wujt.com.wujt.shiro.jwt.AccessInfo;
import com.wujt.com.wujt.shiro.jwt.JWTUtil;
import com.wujt.com.wujt.shiro.jwt.JwtAuthenticationToken;
import com.wujt.com.wujt.shiro.spi.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author wujt
 */
@Slf4j
@Component
public class JWTRealm extends AuthorizingRealm {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    public JWTRealm(JWTUtil jwtUtil) {
        // token 为需要验证的数据，info为获取的认证信息，进行匹配，jwt方案只需要对token进行自验
        this.setCredentialsMatcher((AuthenticationToken token, AuthenticationInfo info) -> {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) token;
            try {
                boolean refresh = jwtToken.isRefresh();
                if (refresh) {
                    return jwtUtil.verifyRefreshToken(jwtToken.getToken(), jwtToken.getAccessInfo());
                } else {
                    return jwtUtil.verifyToken(jwtToken.getToken(), jwtToken.getAccessInfo());
                }
            } catch (JWTVerificationException e) {
                log.error(e.getMessage());
            }
            return false;
        });
        // 设置认证缓存和授权缓存的名称, redis 缓存关闭
        // this.setAuthorizationCacheName(ShiroConstant.AUTHORIZATION_CACHE_NAME);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("check permission");
        JwtAuthenticationToken token = (JwtAuthenticationToken) principalCollection.getPrimaryPrincipal();
        AccessInfo accessInfo = token.getAccessInfo();
        if (accessInfo == null) {
            throw new RuntimeException("ErrorCodeEnum.UNLOGIN");
        }
        String userKey = accessInfo.getUserKey();
        Set<String> permissions = permissionService.getPermissions(userKey);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    /**
     * 获取认证信息，一般是数据库，jwt验证只需要自身验证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authenticationToken;
        // Matcher主要是对主题进行jwt 校验，credential没有利用， 设置principal，方便当前线程获取信息
        return new SimpleAuthenticationInfo(jwtAuthenticationToken, "", getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        // 仅支持JwtToken类型的Token
        return token instanceof JwtAuthenticationToken;
    }

}
