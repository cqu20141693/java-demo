package com.wujt.com.wujt.shiro.config;

import com.wujt.com.wujt.extend.spi.PermisssionServiceImpl;
import com.wujt.com.wujt.shiro.jwt.JWTFilter;
import com.wujt.com.wujt.shiro.jwt.JWTUtil;
import com.wujt.com.wujt.shiro.realm.JWTRealm;
import com.wujt.com.wujt.shiro.realm.UserPasswordRealm;
import com.wujt.com.wujt.shiro.spi.PermissionService;
import lombok.Data;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.*;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujt
 */
@Configuration
@Data
public class ShiroConfiguration {


    /**
     * 二.权限管理
     *
     * @param userPasswordRealm
     * @param jwtRealm
     * @return
     * @Description: SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理
     */
    @Bean
    public SessionsSecurityManager securityManager(UserPasswordRealm userPasswordRealm, JWTRealm jwtRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> realms = new ArrayList<Realm>(16);
        realms.add(userPasswordRealm);
        realms.add(jwtRealm);
        securityManager.setRealms(realms);
        // 配置session管理器
        // 3.关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

    /**
     * 禁用session, 不保存用户登录状态。保证每次请求都重新认证
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    // ShiroFactory

    /**
     * 一.请求拦截
     *
     * @param securityManager
     * @return
     */
    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, JWTUtil jwtUtil) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 添加拦截器
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        // 这里必须是new 创建
        filters.put("jwtFilter", new JWTFilter(jwtUtil));
        Map<String, String> filterMap = new LinkedHashMap<String, String>();
        //配置资源拦截器，shiro 自带 anon：匿名拦截器，authc：认证拦截器，roles：角色授权拦截器
        //anon. 配置不会被拦截的请求 顺序判断
        filterMap.put("/sso/logout", "anon");
        filterMap.put("/sso/login", "anon");
        filterMap.put("/health", "anon");
        filterMap.put("/sso/token/validate", "anon");

        // 配置拦截的请求（对所有请求先认证后授权）
        filterMap.put("/**", "jwtFilter");
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }


    @Bean
    @ConditionalOnMissingBean({PermissionService.class})
    public PermissionService permissionService(){
        return new PermisssionServiceImpl();
    }
}
