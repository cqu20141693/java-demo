package com.cc.ocpp.client;

import com.cc.ocpp.network.cp.domian.Version;
import lombok.Data;

/**
 * wcc 2022/5/6
 */
@Data
public class Session {
    private Short token = 0;
    private Version version;
}
