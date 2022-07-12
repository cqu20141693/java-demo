package com.cc.sip;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * wcc 2022/5/25
 */
@Data
@AllArgsConstructor(staticName = "of")
public class SipConfig {
    /**
     * 视频网关Id
     */
    private String mediaGatewayId;

    /**
     * 流媒体服务表主键
     */
    private String mediaConfigId;

    /**
     * 流媒体服务器ID
     */
    private String mediaServerId;

    /**
     * 网关关联的产品ID
     */
    private String productId;

    /**
     * 流媒体供应商 ZLM  SRS
     */
    private String mediaSerProvider;

    /**
     * 网络组件ID
     */
    private String networkId;

    /**
     * SIP配置
     */
    private Map<String, Object> sipConfiguration;

    /**
     * 流媒体配置
     */
    private Map<String, Object> mediaConfiguration;
}
