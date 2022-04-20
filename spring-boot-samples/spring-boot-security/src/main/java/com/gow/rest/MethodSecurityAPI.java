package com.gow.rest;

import com.gow.common.Result;
import com.gow.security.privisioning.MutableUser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gow
 * @date 2021/8/5
 */
@RestController
@RequestMapping("security")
public class MethodSecurityAPI {

    @GetMapping("getRole")
    @Secured(value = {"admin"})
    public Result<String> getRole(Authentication authentication){

        MutableUser user = (MutableUser) authentication.getPrincipal();

        return Result.ok(user.getAuthorities().toString());
    }
}
