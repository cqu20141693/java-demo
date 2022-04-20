package com.gow.camera.api.lecheng.bussiness.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gow.camera.api.lecheng.bussiness.DeviceManager;
import com.gow.camera.api.lecheng.model.resp.DeviceStatus;
import com.gow.camera.api.lecheng.model.resp.DeviceTime;
import com.gow.camera.api.lecheng.model.resp.MethodType;
import com.gow.camera.api.lecheng.model.resp.ResponseResult;
import com.gow.camera.api.lecheng.util.HttpSend;
import java.util.HashMap;

/**
 * @author gow
 * @date 2021/8/12
 */
public class DeviceManagerImpl implements DeviceManager {
    @Override
    public ResponseResult<DeviceStatus> deviceOnline(HashMap<String, Object> paramMap) {
        String execute = HttpSend.execute(paramMap, MethodType.DEVICE_ONLINE.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }
    }

    @Override
    public ResponseResult<DeviceTime> getDeviceTime(HashMap<String, Object> paramMap) {
        String execute = HttpSend.execute(paramMap, MethodType.GET_DEVICE_TIME.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }
    }

}
