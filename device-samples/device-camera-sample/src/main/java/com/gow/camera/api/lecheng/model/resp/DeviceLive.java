package com.gow.camera.api.lecheng.model.resp;

import java.util.List;
import lombok.Data;

/**
 * @author gow
 * @date 2021/8/12
 */
@Data
public class DeviceLive {
    //	直播token
    private String liveToken;
    //直播状态（1：开启；2：暂停；3：流量不足）
    private Integer liveStatus;
    //直播源类型（1：设备；2：流地址）
    private Integer liveType;
    //	[可选]设备序列号
    private String deviceId;
    // 通道号
    private Integer channelId;
    //视频封面更新频率（单位：s）
    private Integer coverUpdate;
    private List<ChannelStream> streams;
    private List<Job> job;
}
