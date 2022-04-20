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
public class AppAuthToken extends AuthToken {
    /**
     * app unique key
     */
    private String appKey;
    /**
     * App Token, available in plain text
     * recommend HmacSha1(password,userKey)，but there are still replay attacks
     * {HmacSha1(password,{userKey,nonce,timestamp}),nonce,timestamp}
     */
    private String appToken;

    @Override
    public String toString() {
        String signPassword = getRealToken(appKey, appToken);

        AppAuthToken authToken = new AppAuthToken();
        authToken.setAppKey(appKey);
        authToken.setAppToken(signPassword);
        authToken.setTokenType(tokenType);
        return JSONObject.toJSONString(authToken);
    }

}
