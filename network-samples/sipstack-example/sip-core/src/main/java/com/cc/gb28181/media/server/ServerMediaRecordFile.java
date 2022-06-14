package com.cc.gb28181.media.server;

import lombok.Getter;
import lombok.Setter;

/**
 * 服务端录像记录信息
 * wcc 2022/6/3
 */
@Getter
@Setter
public class ServerMediaRecordFile extends MediaInfo {

    /**
     * 视频流ID
     */
    private String streamId;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 录像文件时间点
     */
    private long time;

}
