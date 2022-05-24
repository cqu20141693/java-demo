package com.cc.netwok.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * wcc 2021/6/18
 */
@Data
@Accessors(chain = true)
public class ChannelAliveEvent {
    /**
     * channel Alive check seconds
     */
    private Integer channelAliveCheckTime;

}
