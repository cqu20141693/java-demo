package com.cc.gb28181.invoker;

import com.cc.sip.SipDeviceMessage;
import lombok.Data;

import javax.sip.RequestEvent;
import javax.sip.header.Header;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 处理设备消息
 * wcc 2022/5/24
 */
@Data
public class HandleDeviceMessage {
    private RequestEvent requestEvent;
    private SipDeviceMessage sipDeviceMessage;
    private BiConsumer<Integer, List<Header>> consumer;
}
