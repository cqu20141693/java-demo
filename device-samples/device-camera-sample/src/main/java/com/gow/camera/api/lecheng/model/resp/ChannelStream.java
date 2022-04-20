package com.gow.camera.api.lecheng.model.resp;

import lombok.Data;

/**
 * @author gow
 * @date 2021/8/13
 */
@Data
public class ChannelStream {
    //直播流hls访问地址
    private String hls;
    //视频封面url
    private String coverUrl;
    //码流类型（0:高清主码流；1:标清辅码流）(当直播地址由rtsp源生成时，无该字段)
    private Integer streamId;
}
