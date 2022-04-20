package com.gow.camera.api.lecheng.bussiness.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gow.camera.api.lecheng.bussiness.DeviceLiveManager;
import com.gow.camera.api.lecheng.model.resp.DeviceLive;
import com.gow.camera.api.lecheng.model.resp.DeviceLiveInfo;
import com.gow.camera.api.lecheng.model.resp.LiveStatus;
import com.gow.camera.api.lecheng.model.resp.LiveStreamInfo;
import com.gow.camera.api.lecheng.model.resp.MethodType;
import com.gow.camera.api.lecheng.model.resp.ResponseResult;
import com.gow.camera.api.lecheng.util.HttpSend;
import java.util.Map;

/**
 * @author gow
 * @date 2021/8/12
 */
public class DeviceLiveManagerImpl implements DeviceLiveManager {
    @Override
    public ResponseResult<DeviceLive> bindDeviceLive(Map<String, Object> params) {

        String execute = HttpSend.execute(params, MethodType.BIND_DEVICE_LIVE.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }
    }

    @Override
    public ResponseResult<Void> unbindLive(Map<String, Object> params) {
        String execute = HttpSend.execute(params, MethodType.UNBIND_LIVE.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }
    }

    @Override
    public ResponseResult<DeviceLiveInfo> liveList(Map<String, Object> params) {
        String execute = HttpSend.execute(params, MethodType.LIVE_LIST.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }
    }

    @Override
    public ResponseResult<LiveStatus> queryLiveStatus(Map<String, Object> params) {
        String execute = HttpSend.execute(params, MethodType.GET_LIVE_STATUS.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }
    }

    @Override
    public ResponseResult<Void> modifyLivePlanStatus(Map<String, Object> params) {
        String execute = HttpSend.execute(params, MethodType.MODIFY_LIVE_PLAN_STATUS.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }
    }

    @Override
    public ResponseResult<Void> modifyLivePlan(Map<String, Object> params) {
        String execute = HttpSend.execute(params, MethodType.MODIFY_LIVE_PLAN.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }
    }

    @Override
    public ResponseResult<Void> batchModifyLivePlan(Map<String, Object> params) {
        String execute = HttpSend.execute(params, MethodType.BATCH_MODIFY_LIVE_PLAN.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }
    }

    @Override
    public ResponseResult<LiveStreamInfo> getLiveStreamInfo(Map<String, Object> params) {
        String execute = HttpSend.execute(params, MethodType.GET_LIVE_STREAM_INFO.getName());
        if (execute == null) {
            return null;
        } else {
            return JSONObject.parseObject(execute, new TypeReference<>() {
            });
        }
    }
}
