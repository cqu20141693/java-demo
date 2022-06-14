package com.cc.sip;

import gov.nist.core.LogLevels;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Properties;

/**
 * sip server config
 * wcc 2022/5/14
 */
@Data
public class SipProperties {
    @NotBlank(message = "[sipId]不能为空")
    @Schema(description = "SIP ID")
    private String sipId;

    @NotBlank
    @Schema(description = "SIP 域")
    @NotBlank(message = "[domain]不能为空")
    private String domain;

    @NotBlank
    @Hidden
    private String stackName = "org.wcc.pro.media.gb28181";

    @Schema(description = "字符集")
    private String charset = "GB2312";

    //接入密码
    @Schema(description = "接入密码")
    private String password;

    @Schema(description = "SIP 公网地址")
    private String publicAddress = "127.0.0.1";

    @NotBlank
    @Schema(description = "SIP 本地地址")
    private String localAddress = "0.0.0.0";

    @Schema(description = "端口")
    private int port=5060;

    @Schema(description = "公网端口")
    private int publicPort=5060;

    private Map<String, String> options;

    public int getPublicPort() {
        if (publicPort <= 0) {
            return port;
        }
        return publicPort;
    }
    public String getHostAndPort() {
        return publicAddress + ":" + getPublicPort();
    }
    public Properties toSiProperties() {
        Properties properties = new Properties();
        if (options != null) {
            properties.putAll(options);
        }
        properties.putIfAbsent("gov.nist.javax.sip.TRACE_LEVEL", LogLevels.TRACE_NONE);
        properties.putIfAbsent("gov.nist.javax.sip.SERVER_LOG", "");
        properties.putIfAbsent("gov.nist.javax.sip.DEBUG_LOG", "");
        properties.putIfAbsent("gov.nist.javax.sip.LOG_MESSAGE_CONTENT", "false");
        properties.setProperty("javax.sip.STACK_NAME", stackName);
        properties.setProperty("javax.sip.IP_ADDRESS", localAddress);
        return properties;
    }
}
