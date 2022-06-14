package com.cc.gb28181.media.server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Getter
@Setter
public class StreamInfo extends MediaInfo {

    public static String TYPE_LIVE = "live";

    public static String TYPE_PLAYBACK = "playback";

    public static String TYPE_DOWNLOAD = "download";

    //服务端录制的流
    public static String TYPE_LIVE_RECORD = "live_record";
    public static String TYPE_PROXY_RECORD = "proxy_record";
    public static String TYPE_PLAYBACK_RECORD = "playback_record";


    @Nonnull
    private String streamId;

    //rtp推流时的ssrc值
    @Nullable
    private String ssrc;

    @Schema(description = "流类型")
    private String type;

    @Schema(description = "文件大小")
    private Long fileSize;

    private Long startTime;

    private Long endTime;

    private String sdp;

    public boolean isForRecord() {
        return TYPE_LIVE_RECORD.equals(type) || TYPE_PLAYBACK_RECORD.equals(type);
    }
}
