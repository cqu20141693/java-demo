package com.wujt.com.wujt.shiro.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.wujt.com.wujt.shiro.jwt.JWTUtil.getCookie;

/**
 * 通过拦截器拦截处理，通过后将TOKEN 当做主体到JWTRelam中进行校验
 *
 * @author wujt
 */
@Slf4j
@Component
public class JWTFilter extends BasicHttpAuthenticationFilter {


    private JWTUtil jwtUtil;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (jwtUtil.getCros() && httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
        // 添加跨域支持
        this.fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response));
    }

    /**
     * 添加跨域支持
     *
     * @param httpServletRequest
     * @param httpServletResponse
     */
    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
    }


    /**
     * 过滤器拦截请求的入口方法
     * 返回 true 则允许访问
     * 返回 false 则禁止访问，会进入 onAccessDenied()
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 原用来判断是否是登录请求，在本例中不会拦截登录请求，用来检测Header中是否包含 JWT token 字段
        if (this.isLoginRequest(request, response)) {
            // header中不含JWT token字段，直接访问拒绝
            return false;
        }
        boolean allowed = false;
        try {
            // 检测Header里的JWT token内容是否正确，尝试使用token进行登录
            allowed = executeLogin(request, response);
        } catch (IllegalStateException e) { // not found any token
            log.error("Not found any token");
        } catch (Exception e) {
            log.error("Error occurs when login", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    /**
     * 检测Header中是否包含 JWT token字段 (在判断是否是登录请求时会用到，根据自定义的方法判断header中是否有authenticationHeader字段来判断)
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        Cookie cookie = getCookie(request, jwtUtil.getCookie());
        Cookie refreshCookie = getCookie(request, jwtUtil.getRefreshCookie());
        if (cookie == null) {
            if (refreshCookie == null) {
                return true;
            }
            // cookie过期了，重新生成cookie和refreshCookie
            AccessInfo accessInfo = JWTUtil.getAccessInfo(refreshCookie.getValue());
            jwtUtil.setCookie(WebUtils.toHttp(request), WebUtils.toHttp(response), accessInfo);
        }
        return false;
    }

    /**
     * 身份验证,检查 JWT token 是否合法
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        AuthenticationToken token = createToken(request, response);
        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken "
                    + "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
        try {
            Subject subject = getSubject(request, response);
            // 交给Shiro去进行登录验证
            // 由于这里创建的是JwtToken，因此会进入JwtRealm中进行身份校验
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            // cookie 清理
            jwtUtil.clearCookie(WebUtils.toHttp(request),WebUtils.toHttp(response));
            return onLoginFailure(token, e, request, response);
        }
    }

    /**
     * 从cookie里提取JWT token
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {

        Cookie cookie = getCookie(request, jwtUtil.getCookie());
        String token;
        boolean refresh = false;
        if (cookie == null) {
            // cookie为空则使用refresh cookie
            Cookie refreshCookie = getCookie(request, jwtUtil.getRefreshCookie());
            if (refreshCookie == null) {
                log.error("refreshCookie is null");
                return null;
            }
            token = refreshCookie.getValue();
            refresh = true;
        } else {
            token = cookie.getValue();
        }
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
        authenticationToken.setRefresh(refresh);

        return authenticationToken;
    }

    /**
     * isAccessAllowed()方法返回false，会进入该方法，表示拒绝访问
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        // 返回固定的JSON串
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * <p>
     * 配置了DefaultAdvisorAutoProxyCreator这个bean会出现两次调用doGetAuthorizationInfo的问题，故这里不进行配置
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
