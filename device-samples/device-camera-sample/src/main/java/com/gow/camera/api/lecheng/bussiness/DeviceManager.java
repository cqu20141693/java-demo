package com.gow.camera.api.lecheng.bussiness;

import com.gow.camera.api.lecheng.model.resp.DeviceStatus;
import com.gow.camera.api.lecheng.model.resp.DeviceTime;
import com.gow.camera.api.lecheng.model.resp.ResponseResult;
import java.util.HashMap;

/**
 * @author gow
 * @date 2021/8/12
 */
public interface DeviceManager {

    /**
     * @param paramMap 参数
     */
    ResponseResult<DeviceStatus> deviceOnline(HashMap<String, Object> paramMap);

    // 设备操作

    /**
     * token	String	是			管理员或者子账户accessToken
     * deviceId	String	是			设备序列号
     *
     * @param paramMap 参数
     * @return
     */
    ResponseResult<DeviceTime> getDeviceTime(HashMap<String, Object> paramMap);


}
