package com.gow.gateway.core.domain.auth.token;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gow
 * @date 2021/7/17 0017
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAuthToken extends AuthToken {
    /**
     * unique user id
     */
    private String userKey;
    /**
     * User password, available in plain text
     * recommend HmacSha1(password,userKey)，but there are still replay attacks
     * {HmacSha1(password,{userKey,nonce,timestamp}),nonce,timestamp}
     */
    private String password;


    @Override
    public String toString() {
        String realToken = getRealToken(userKey, password);
        UserAuthToken userAuthToken = new UserAuthToken();
        userAuthToken.setUserKey(userKey);
        userAuthToken.setPassword(realToken);
        userAuthToken.setTokenType(tokenType);
        return JSONObject.toJSONString(userAuthToken);
    }

    public static void main(String[] args) {
        UserAuthToken userAuthToken = new UserAuthToken();
        userAuthToken.setUserKey("admin");
        userAuthToken.setPassword("123456");
        System.out.println(userAuthToken);
    }
}
