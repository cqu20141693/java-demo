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
public class GDeviceAuthToken extends DeviceBaseAuthToken {
    /**
     * group unique login key
     */
    private String loginKey;
    /**
     * device sn
     */
    private String sn;
    /**
     * group Token, available in plain text
     * recommend HmacSha1(password,userKey)，but there are still replay attacks
     * {HmacSha1(password,{userKey,nonce,timestamp}),nonce,timestamp}
     */
    private String groupToken;

    @Override
    public String toString() {
        String signPassword = getRealToken(loginKey + "#" + sn, groupToken);

        GDeviceAuthToken deviceAuthToken = new GDeviceAuthToken();
        deviceAuthToken.setLoginKey(loginKey);
        deviceAuthToken.setSn(sn);
        deviceAuthToken.setGroupToken(signPassword);
        deviceAuthToken.setTokenType(tokenType);
        deviceAuthToken.setDeviceTokenType(deviceTokenType);
        return JSONObject.toJSONString(deviceAuthToken);
    }

}
