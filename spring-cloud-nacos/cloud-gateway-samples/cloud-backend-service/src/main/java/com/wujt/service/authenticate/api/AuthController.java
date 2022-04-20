package com.wujt.service.authenticate.api;

import com.alibaba.fastjson.JSONObject;
import com.gow.common.Result;
import com.wujt.backend.domain.authenticate.DDeviceAuthReq;
import com.wujt.backend.domain.authenticate.UserAuthReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gow
 * @date 2021/7/18 0018
 */
@RestController
@RequestMapping("api/auth")
@Slf4j
public class AuthController {

    @Value("${auth.user.admin:admin}")
    private String adminUser;

    @Value("${auth.device.admin:gateway}")
    private String adminDevice;

    @PostMapping("userAuth")
    Result<Boolean> userAuth(@RequestBody UserAuthReq userAuthReq) {


        log.info("user auth req={}", JSONObject.toJSONString(userAuthReq));
        if (adminUser.equals(userAuthReq.getUserKey())) {
            return Result.ok(true);
        }
        return Result.ok(false);
    }

    @PostMapping("deviceAuth")
    Result<Boolean> deviceAuth(@RequestBody DDeviceAuthReq dDeviceAuthReq) {


        log.info("dDevice auth req={}", JSONObject.toJSONString(dDeviceAuthReq));
        if (adminDevice.equals(dDeviceAuthReq.getDeviceKey())) {
            return Result.ok(true);
        }
        return Result.ok(false);
    }
}
