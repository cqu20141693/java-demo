package com.gow.camera.api.lecheng.bussiness.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gow.camera.api.lecheng.bussiness.UserManager;
import com.gow.camera.api.lecheng.model.resp.AccessToken;
import com.gow.camera.api.lecheng.model.resp.MethodType;
import com.gow.camera.api.lecheng.model.resp.ResponseResult;
import com.gow.camera.api.lecheng.util.HttpSend;
import java.util.HashMap;

public class UserManagerImpl implements UserManager {

    @Override
    public ResponseResult<AccessToken> accessToken(HashMap<String, Object> paramMap) {
        String execute = HttpSend.execute(paramMap, MethodType.ACCESS_TOKEN.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }
    }

}
