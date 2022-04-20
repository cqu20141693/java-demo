package com.gow.backend.authenticate.client.fallback;

import com.gow.common.Result;
import com.gow.backend.authenticate.client.AuthenticateClient;
import com.wujt.backend.domain.authenticate.DDeviceAuthReq;
import com.wujt.backend.domain.authenticate.UserAuthReq;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gow
 * @date 2021/7/18 0018
 */
@Component
@RequestMapping("api/auth/fallback")
public class AuthenticateClientFallback implements AuthenticateClient {
    @Override
    public Result<Boolean> userAuth(UserAuthReq userAuthReq) {
        return null;
    }

    @Override
    public Result<Boolean> deviceAuth(DDeviceAuthReq dDeviceAuthReq) {
        return null;
    }
}
