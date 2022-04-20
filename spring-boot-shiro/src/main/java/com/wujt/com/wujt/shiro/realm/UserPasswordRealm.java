package com.wujt.com.wujt.shiro.realm;

import com.wujt.com.wujt.shiro.domain.ShiroSimpleByteSource;
import com.wujt.com.wujt.shiro.domain.SysUserInfo;
import com.wujt.com.wujt.shiro.spi.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配置自定义shiro认证
 * 继承AuthorizingRealm（默认开启授权缓存） -》继承AuthenticatingRealm（默认关闭认证缓
 *
 * @author wujt
 */
@Component
@Slf4j
public class UserPasswordRealm extends AuthenticatingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    public UserPasswordRealm() {
        this.setCredentialsMatcher(hashedCredentialsMatcher());

        // 这里不需要认证缓存，暂不打开
        // 开启认证缓存（实际上开启这个缓存，cachingEnabled也会被置为true）
//        myShiroRealm.setAuthenticationCachingEnabled(true);
//        myShiroRealm.setAuthenticationCacheName("authentication");
    }

    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        // 散列的次数，比如散列两次，相当于 md5(md5(""))
        hashedCredentialsMatcher.setHashIterations(2);
        // 转化为16进制(与入库时保持一致)
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取认证token
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        // 通过username从数据库中查找
        SysUserInfo user = userService.getByName(usernamePasswordToken.getUsername());
        if (user == null) {
            return null;
        }
        if (!user.valid()) {
            throw new LockedAccountException();
        }

        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,
                user.getPassword(),
                //ByteSource.Util.bytes(user.getSalt()),
                new ShiroSimpleByteSource(user.getSalt()),
                getName()
        );

        return authenticationInfo;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        // 仅支持UsernamePasswordToken类型的Token
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }
}
