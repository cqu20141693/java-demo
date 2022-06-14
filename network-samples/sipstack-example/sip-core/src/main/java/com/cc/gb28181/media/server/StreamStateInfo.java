package com.cc.gb28181.media.server;

import lombok.Getter;
import lombok.Setter;

/**
 * 流状态信息
 * wcc 2022/6/3
 */
@Getter
@Setter
public class StreamStateInfo {

    private String serverId;

    private String streamId;

    private int players;

}
