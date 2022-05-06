package com.cc.network.cp.domian;

import com.alibaba.fastjson.JSONObject;
import com.cc.network.cp.domian.login.ManufacturerInfo;
import org.junit.jupiter.api.Test;

class ManufacturerInfoTest {
    @Test
    public void test() {
        ManufacturerInfo info = new ManufacturerInfo(new byte[]{0x57, 0x31, 0x30, 0x31, 0x03});
        byte[] encode = info.getPayload();
        System.out.println(JSONObject.toJSONString(info));
    }

}
