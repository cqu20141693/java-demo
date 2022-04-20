package com.gow.security.jwt;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gow.jwt.JWTUtil;
import com.gow.jwt.domain.AccessInfo;
import com.gow.jwt.domain.JWTConfig;
import com.gow.jwt.domain.Tokens;
import com.gow.security.privisioning.MutableUser;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/25
 */
@Component
@Slf4j
public class JWTAuthenticationFilter extends GenericFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private JWTConfig jwtConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("JWTAuthenticationFilter passed");

        Tokens tokens = jwtUtil.getTokens((HttpServletRequest) request, (HttpServletResponse) response, jwtConfig);
        if (!tokens.getToken().isBlank()) {

            AccessInfo accessInfo = jwtUtil.verifyToken(tokens.getToken(), jwtConfig);
            if (accessInfo == null) {
                jwtAuthFailed(request, response);
                return;
            }

            jwtAuthSuccess(accessInfo);

        } else {
            // refresh
            if (!tokens.getRefreshToken().isBlank()) {
                AccessInfo accessInfo = jwtUtil.verifyRefreshToken(tokens.getRefreshToken(), jwtConfig);
                if (accessInfo != null) {
                    jwtUtil.setCookie((HttpServletRequest) request, (HttpServletResponse) response, accessInfo,
                            jwtConfig);

                    jwtAuthSuccess(accessInfo);

                } else {
                    jwtAuthFailed(request, response);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    private void jwtAuthSuccess(AccessInfo accessInfo) {
        String userName = accessInfo.getUserKey();
        String extend = accessInfo.getExtend();
        TokenInfo tokenInfo = JSONObject.parseObject(extend, TokenInfo.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(new MutableUser(tokenInfo.getJwtUser()), "",
                        tokenInfo.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void jwtAuthFailed(ServletRequest request, ServletResponse response) throws IOException {
        // 清理cookie,并返回401
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", 401);
        resp.put("msg", "Identity authentication failed");
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(resp);
        jwtUtil.clearCookie((HttpServletRequest) request, (HttpServletResponse) response, jwtConfig);
        ((HttpServletResponse) response).setStatus(401);
        response.getWriter().write(s);
    }
}
