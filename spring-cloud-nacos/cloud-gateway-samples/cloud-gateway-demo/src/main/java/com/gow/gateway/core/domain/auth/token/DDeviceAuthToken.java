package com.gow.gateway.core.domain.auth.token;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gow
 * @date 2021/7/18 0018
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DDeviceAuthToken extends DeviceBaseAuthToken {
    /**
     * device unique key
     */
    private String deviceKey;
    /**
     * device Token, available in plain text
     * recommend HmacSha1(password,userKey)，but there are still replay attacks
     * {HmacSha1(password,{userKey,nonce,timestamp}),nonce,timestamp}
     */
    private String deviceToken;

    @Override
    public String toString() {
        assert deviceTokenType != null : "deviceTokenType can not be null";
        String signPassword = getRealToken(deviceKey, deviceToken);

        DDeviceAuthToken deviceAuthToken = new DDeviceAuthToken();
        deviceAuthToken.setDeviceKey(deviceKey);
        deviceAuthToken.setDeviceToken(signPassword);
        deviceAuthToken.setTokenType(tokenType);
        deviceAuthToken.setDeviceTokenType(deviceTokenType);
        return JSONObject.toJSONString(deviceAuthToken);
    }

}
