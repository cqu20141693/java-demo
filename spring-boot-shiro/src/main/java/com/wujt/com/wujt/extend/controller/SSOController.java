package com.wujt.com.wujt.extend.controller;

import com.wujt.com.wujt.extend.model.UserInfo;
import com.wujt.com.wujt.extend.model.UserLoginDTO;
import com.wujt.com.wujt.extend.model.UserLoginVO;
import com.wujt.com.wujt.shiro.jwt.AccessInfo;
import com.wujt.com.wujt.shiro.jwt.JWTUtil;
import com.wujt.com.wujt.shiro.jwt.JwtAuthenticationToken;
import com.wujt.com.wujt.util.ResponseMessage;
import com.wujt.com.wujt.util.SSOErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author wujt
 */
@RestController
@RequestMapping("sso")
@Slf4j
public class SSOController {

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("login")
    public ResponseMessage login(@RequestBody @Validated UserLoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        log.info("access login");
        try {
            // 封装登录信息
            UsernamePasswordToken token = new UsernamePasswordToken(loginDTO.getUsername(), loginDTO.getPassword());
            Subject subject = SecurityUtils.getSubject();
            // 登录
            subject.login(token);
            // 登录成功，签发JWT Token
            UserInfo sysUser = (UserInfo) SecurityUtils.getSubject().getPrincipal();

            AccessInfo accessInfo = new AccessInfo(sysUser.getUsername(), sysUser.getUserKey());
            String jwtToken = jwtUtil.setCookie(request, response, accessInfo);
            UserLoginVO userVO = new UserLoginVO();
            BeanUtils.copyProperties(sysUser, userVO);
            userVO.setToken(jwtToken);
            return ResponseMessage.success(userVO);
        } catch (IncorrectCredentialsException e) {
            log.error("password error");
            return ResponseMessage.error(SSOErrorCode.USERNAME_PASSWORD_ERROR.getCode(), SSOErrorCode.USERNAME_PASSWORD_ERROR.getMessage());
        } catch (LockedAccountException e) {
            log.error("account is locked");
            return ResponseMessage.error(SSOErrorCode.USERNAME_PASSWORD_ERROR.getCode(), SSOErrorCode.USERNAME_PASSWORD_ERROR.getMessage());
        } catch (AuthenticationException e) {
            log.error("user is not exist");
            return ResponseMessage.error(SSOErrorCode.USERNAME_PASSWORD_ERROR.getCode(),
                    SSOErrorCode.USERNAME_PASSWORD_ERROR.getMessage());
        }
    }

    @GetMapping("test")
    @RequiresPermissions("add")
    public String test() {
        return "test";
    }


    /**
     * 用于cookie 验证
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("authenticate")
    public ResponseMessage authenticate(HttpServletRequest request, HttpServletResponse response) {
        // JWTFilter 会对请求做验证
        JwtAuthenticationToken principal = (JwtAuthenticationToken) SecurityUtils.getSubject().getPrincipal();
        HashMap<String, Object> map = new HashMap<>();
        map.put("refresh", principal.isRefresh());
        map.put("accessInfo", principal.getAccessInfo());
        return ResponseMessage.success(map);
    }

    /**
     * token 验证，通过返回正真的cookie
     * 该token再极短的时间内有效，并且和cookie的密码不一致
     *
     * @param token
     * @return
     */
    @PostMapping("token/validate")
    public ResponseMessage tokenValidate(@RequestParam("token") String token) {
        // 首先验证token,然后将数据取出进行封装为cookie和refreshCookie
        return ResponseMessage.success();
    }

    @PostMapping("logout")
    public ResponseMessage logout(HttpServletRequest request, HttpServletResponse response) {

        return ResponseMessage.success();
    }
}
