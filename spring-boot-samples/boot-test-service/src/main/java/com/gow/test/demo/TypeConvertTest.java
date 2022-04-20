package com.gow.test.demo;

import com.alibaba.fastjson.JSONObject;
import com.gow.codec.base.TypeIntConvert;
import com.gow.codec.base.TypeJsonConvert;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author gow
 * @date 2021/8/11
 */
public class TypeConvertTest {

    public static void main(String[] args) {

        TypeIntConvert intConvert = new TypeIntConvert();
        TypeJsonConvert jsonConvert = new TypeJsonConvert();
        byte[] bytes = intConvert.convertToBytes(1);
        JSONObject object = JSONObject.parseObject("{1}".getBytes(StandardCharsets.UTF_8), JSONObject.class);
        JSONObject o = JSONObject.parseObject(bytes, JSONObject.class);
        System.out.println(o);

    }
}
