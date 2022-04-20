package com.gow.backend.authenticate.client;

import com.gow.common.Result;
import com.gow.backend.authenticate.client.fallback.AuthenticateClientFallback;
import com.wujt.backend.domain.authenticate.DDeviceAuthReq;
import com.wujt.backend.domain.authenticate.UserAuthReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gow
 * @date 2021/7/18 0018
 */
@FeignClient(value = "cloud-backend-service", fallback = AuthenticateClientFallback.class)
@RequestMapping("api/auth")
public interface AuthenticateClient {

    @PostMapping("userAuth")
    Result<Boolean> userAuth(@RequestBody UserAuthReq userAuthReq);

    @PostMapping("deviceAuth")
    Result<Boolean> deviceAuth(@RequestBody DDeviceAuthReq dDeviceAuthReq);
}
