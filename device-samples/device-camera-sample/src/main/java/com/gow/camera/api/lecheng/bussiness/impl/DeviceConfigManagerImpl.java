package com.gow.camera.api.lecheng.bussiness.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gow.camera.api.lecheng.bussiness.DeviceConfigManager;
import com.gow.camera.api.lecheng.model.resp.GetDeviceOsd;
import com.gow.camera.api.lecheng.model.resp.MethodType;
import com.gow.camera.api.lecheng.model.resp.ResponseResult;
import com.gow.camera.api.lecheng.util.HttpSend;
import java.util.HashMap;

/**
 * @author gow
 * @date 2021/8/12
 */
public class DeviceConfigManagerImpl implements DeviceConfigManager {
    @Override
    public ResponseResult<GetDeviceOsd> queryDeviceOsd(HashMap<String, Object> paramMap) {
        String execute = HttpSend.execute(paramMap, MethodType.GET_DEVICE_OSD.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }

    }

    @Override
    public ResponseResult<GetDeviceOsd> frameReverseStatus(HashMap<String, Object> paramMap) {
        String execute = HttpSend.execute(paramMap, MethodType.FRAME_REVERSE_STATUS.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }
    }
}
