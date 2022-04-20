package com.gow.camera.api.lecheng.bussiness;

import com.gow.camera.api.lecheng.model.resp.DeviceLive;
import com.gow.camera.api.lecheng.model.resp.DeviceLiveInfo;
import com.gow.camera.api.lecheng.model.resp.LiveStatus;
import com.gow.camera.api.lecheng.model.resp.LiveStreamInfo;
import com.gow.camera.api.lecheng.model.resp.ResponseResult;
import java.util.Map;

/**
 * @author gow
 * @date 2021/8/12
 */
public interface DeviceLiveManager {

    /**
     * token	String	是			管理员accessToken
     * deviceId	String	是			设备序列号
     * channelId	String	是			通道号
     * streamId	Integer	是			码流类型,0:高清主码流；1:标清辅码流
     * liveMode	String	否			直播类型 “proxy” 此参数可不填写，或固定填“proxy”
     *
     * @param params 参数
     */
    ResponseResult<DeviceLive> bindDeviceLive(Map<String, Object> params);

    /**
     * token	String	是			管理员accessToken
     * liveToken	String	是			直播token
     *
     * @param params 参数
     */
    ResponseResult<Void> unbindLive(Map<String, Object> params);

    /**
     * token	String	是			管理员accessToken
     * queryRange	String	是			查询范围 数字取值范围为：[1，N] （N为正整数，且N>1）;差值范围为：[0,99]
     *
     * @param params 参数
     */
    ResponseResult<DeviceLiveInfo> liveList(Map<String, Object> params);

    /**
     * token	String	是			管理员accessToken
     * liveToken	String	是			直播token
     *
     * @param params 参数
     */
    ResponseResult<LiveStatus> queryLiveStatus(Map<String, Object> params);

    /**
     * token	String	是			管理员accessToken
     * liveToken	String	是			直播token
     * status	String	是			状态，on：开启，off：关闭
     *
     * @param params 参数
     */
    ResponseResult<Void> modifyLivePlanStatus(Map<String, Object> params);

    /**
     * token	String	是			管理员accessToken
     * liveToken	String	是			直播Token
     * period	String	是			直播周期，always：永久，once：指定某天, everyday：每天
     * beginTime	String	否			开始时间,"period"为"always"时，可为空； "period"为"once"时，时间格式为"yyyy-MM-dd HH:mm:ss"；
     * "period"为"everyday"时，时间格式为"HH:mm:ss"；平台保存时，秒位默认保存00
     * endTime	String	否			结束时间,"period"为"always"时，可为空； "period"为"once"时，时间格式为"yyyy-MM-dd HH:mm:ss"，且大于当前时间；
     * "period"为"everyday"时，时间格式为"HH:mm:ss"；平台保存时，秒位默认保存00
     *
     * @param params 参数
     */
    ResponseResult<Void> modifyLivePlan(Map<String, Object> params);

    /**
     * token	String	是			管理员accessToken
     * liveToken	String	是			直播token，创建直播时返回
     * rules
     * period	String	是		monday，tuesday，wednesday，thursday，friday，saturday，sunday	重复周期
     * beginTime	String	是		时间格式：HH:mm	开始时间
     * endTime	String	是		时间格式：HH:mm	结束时间
     *
     * @param params 参数
     */
    ResponseResult<Void> batchModifyLivePlan(Map<String, Object> params);

    /**
     * token	String	是			管理员accessToken
     * deviceId	String	是			设备序列号
     * channelId	String	是			通道号
     *
     * @param params 参数
     */
    ResponseResult<LiveStreamInfo> getLiveStreamInfo(Map<String, Object> params);


}
