package com.wujt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * use to broker channel event info
 *
 * @author wujt
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChanEventInfo {
    private int userId;
    private String userKey;
    private int productId;
    private String productKey;
    private int deviceId;
    private String deviceKey;
    private Boolean gateway;
    private String dynamicSecret;
    private String sessionKey;
    private long time;
    private int event;
    private String ip;
    private int port;
    private String clientId;
    private Map<String, String> extend;
}
