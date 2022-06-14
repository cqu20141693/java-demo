package com.cc.gb28181.media.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.BiConsumer;

@AllArgsConstructor
@Getter
public enum MediaFormat {

    rtmp("rtmp", "rtmps", MediaInfo::setRtmp),
    rtsp("rtsp", "rtsps", MediaInfo::setRtsp),
    flv("http", "https", MediaInfo::setFlv),
    mp4("http", "https", MediaInfo::setMp4),
    hls("http", "https", MediaInfo::setHls),
    rtc("webrtc", "webrtc", MediaInfo::setRtc);

    private final String schema;
    private final String tlsSchema;
    private final BiConsumer<MediaInfo, String> addressSetter;
}
