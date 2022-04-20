package com.gow.codec.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import java.nio.charset.Charset;

public class TypeJsonConvert implements TypeConversion<JSON> {
    @Override
    public ConvertResponse<JSON> rawDataConvert(byte[] payload) {
        ConvertResponse<JSON> response = new ConvertResponse<>();
        String text = new String(payload);
        JSONValidator.Type type = JSONValidator.from(text).getType();
        if (type == JSONValidator.Type.Object) {
            return response.setConvertResult(JSON.parseObject(text));
        } else if (type == JSONValidator.Type.Array) {
            return response.setConvertResult(JSON.parseArray(text));
        } else {
            return response.setSuccess(false).setFailMsg("payload is not json structure data.");
        }
    }

    @Override
    public JSON strDataConvert(String data) {
        JSONValidator.Type type = JSONValidator.from(data).getType();
        if (type == JSONValidator.Type.Object) {
            return JSON.parseObject(data);
        } else if (type == JSONValidator.Type.Array) {
            return JSON.parseArray(data);
        } else {
            return null;
        }
    }

    @Override
    public byte[] convertToBytes(Object obj) {
        JSON json = (JSON) obj;
        return json.toJSONString().getBytes(Charset.defaultCharset());
    }

    @Override
    public String objectDataConvertStr(Object obj) {
        return ((JSON) obj).toJSONString();
    }

    @Override
    public boolean validType(Object obj) {
        return obj instanceof JSON;
    }
}
