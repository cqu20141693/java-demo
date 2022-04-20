package com.gow.camera.api.lecheng.model.resp;

import lombok.Data;

/**
 * @author gow
 * @date 2021/8/13
 */
@Data
public class LiveStream {
    // 状态，0:正在直播中,1:正在直播中，但是视频封面异常,2:视频源异常,
    // 3:码流转换异常,4:云存储访问异常,10:直播暂停中
    private String status;
    //码流类型（0:高清主码流；1:标清辅码流）
    private Integer streamId;
    // 直播流hls访问地址
    private String hls;
    private String lineToken;
}
