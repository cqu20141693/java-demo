package com.cc.network.cp.domian;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * wcc 2022/4/30
 */
public interface PayLoad {
    @JSONField(serialize = false)
    byte[] getPayload();
}
