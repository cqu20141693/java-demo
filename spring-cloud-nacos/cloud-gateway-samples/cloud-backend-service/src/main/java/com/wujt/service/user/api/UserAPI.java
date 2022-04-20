package com.wujt.service.user.api;

import com.gow.common.Result;
import com.wujt.backend.domain.user.UserInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gow
 * @date 2021/7/18 0018
 */
@RestController
@RequestMapping("api/user")
public class UserAPI {

    @GetMapping("getUserInfo")
    Result<UserInfo> getUserInfo(@RequestParam("userKey") String userKey) {
        return Result.ok(new UserInfo().setUserKey(userKey).setUsername(RandomStringUtils.randomAlphanumeric(8))
                .setRole("admin"));
    }
}
