package com.gow.rest;

import com.gow.common.Result;
import com.gow.model.UserInfo;
import com.gow.security.privisioning.MutableUser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gow
 * @date 2021/7/24
 */
@RestController
@RequestMapping("users")
public class UserController {

    @GetMapping("getInfo")
    public Result<UserInfo> getUserInfo(Authentication authentication) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(authentication.getName());
        MutableUser user = (MutableUser) authentication.getPrincipal();
        userInfo.setUserId(user.getUserId());
        return Result.ok(userInfo);
    }
}
