package com.gow.security.authenticate;

import com.alibaba.fastjson.JSONObject;
import com.gow.jwt.JWTUtil;
import com.gow.jwt.domain.AccessInfo;
import com.gow.jwt.domain.JWTConfig;
import com.gow.jwt.domain.PlatformEnum;
import com.gow.jwt.domain.Tokens;
import com.gow.security.privisioning.MutableUser;
import com.gow.security.jwt.TokenInfo;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/26
 */
@Component
public class AuthenticateSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private JWTConfig jwtConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        HashMap<String, Object> result = new HashMap<>();
        // 获取用户权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // User can be customized later，such as add userKey
        MutableUser user = (MutableUser) authentication.getPrincipal();
        result.put("userId", user.getUserId());
        result.put("username", user.getUsername());

        AccessInfo accessInfo = new AccessInfo();
        accessInfo.setUserKey(user.getUserId());
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setJwtUser(user.getDelegate());
        HashSet<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        for (GrantedAuthority authority : user.getAuthorities()) {
            grantedAuthorities.add((SimpleGrantedAuthority) authority);
        }
        tokenInfo.setAuthorities(grantedAuthorities);
        accessInfo.setExtend(JSONObject.toJSONString(tokenInfo));
        accessInfo.setPlatform(PlatformEnum.agriculture);
        // 默认提供cookie的方式
        Tokens tokens = jwtUtil.setCookie(httpServletRequest, httpServletResponse, accessInfo, jwtConfig);
        // tokens 返回可用于本地缓存后通过header进行传输
        result.put("tokens", tokens);

        // set jwt cookie
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.getWriter().write(JSONObject.toJSONString(result));


    }

    public Tokens getTokens(AccessInfo accessInfo, JWTConfig config) {
        Tokens tokens = new Tokens();
        String token = jwtUtil.signToken(accessInfo, config);
        String refreshToken = jwtUtil.signRefreshToken(accessInfo, config);
        tokens.setToken(token);
        tokens.setRefreshToken(refreshToken);
        return tokens;
    }

}
