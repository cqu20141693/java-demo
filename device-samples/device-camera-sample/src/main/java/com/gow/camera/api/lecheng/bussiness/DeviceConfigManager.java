package com.gow.camera.api.lecheng.bussiness;

import com.gow.camera.api.lecheng.model.resp.GetDeviceOsd;
import com.gow.camera.api.lecheng.model.resp.ResponseResult;
import java.util.HashMap;

/**
 * @author gow
 * @date 2021/8/12
 */
public interface DeviceConfigManager {

    /**
     * 获取设备OSD配置
     * token	String	是			管理员或者子账户accessToken
     * deviceId	String	是			设备序列号
     * channelId	String	是			通道号
     * 配置设备水印需要设备拥有OSD能力集
     *
     * @param paramMap
     * @return
     */
    ResponseResult<GetDeviceOsd> queryDeviceOsd(HashMap<String, Object> paramMap);

    /**
     * 获取设备视频翻转状态
     * token	String	是			管理员或者子账户accessToken
     * deviceId	String	是			设备序列号
     * channelId	String	是			通道号
     *
     * @param paramMap
     * @return
     */
    ResponseResult<GetDeviceOsd> frameReverseStatus(HashMap<String, Object> paramMap);
}
