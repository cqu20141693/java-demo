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
public class GroupAuthToken extends AuthToken {
    /**
     * group unique key
     */
    private String groupKey;
    /**
     * group Token, available in plain text
     * recommend HmacSha1(password,userKey)，but there are still replay attacks
     * {HmacSha1(password,{userKey,nonce,timestamp}),nonce,timestamp}
     */
    private String groupToken;

    @Override
    public String toString() {
        String signPassword = getRealToken(groupKey, groupToken);

        GroupAuthToken authToken = new GroupAuthToken();
        authToken.setGroupKey(groupKey);
        authToken.setGroupToken(signPassword);
        authToken.setTokenType(tokenType);
        return JSONObject.toJSONString(authToken);
    }

}
