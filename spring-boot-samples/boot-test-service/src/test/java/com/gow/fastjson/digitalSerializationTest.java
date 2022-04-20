package com.gow.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.gow.codec.model.EncodeTypeEnum;
import com.gow.test.fastjson.model.ThirdUpMsgModel;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/9/23
 */
public class digitalSerializationTest {

    @Test
    @DisplayName("测试浮点数序列化与反序列化")
    public void testFloat() {
        ThirdUpMsgModel model = new ThirdUpMsgModel();
        Float f = 1F;
        model.setData(f);

        // 浮点数序列化后一定存在小数点
        String jsonString = JSONObject.toJSONString(model);

        ThirdUpMsgModel model1 = JSONObject.parseObject(jsonString, ThirdUpMsgModel.class);
        Object floatData = ThirdUpMsgModel.getData(EncodeTypeEnum.TYPE_FLOAT, model1.getData());
        assert floatData instanceof Float : "hello";

        Object doubleData = ThirdUpMsgModel.getData(EncodeTypeEnum.TYPE_DOUBLE, model1.getData());
        assert doubleData instanceof Float : "hello";
        // 整形数据序列化后没有小数点，反序列化后是整形
        model.setData(1);
        jsonString = JSONObject.toJSONString(model);
        model1 = JSONObject.parseObject(jsonString, ThirdUpMsgModel.class);
        floatData = ThirdUpMsgModel.getData(EncodeTypeEnum.TYPE_INT, model1.getData());

        int disableDecimalFeature = JSON.DEFAULT_PARSER_FEATURE & ~Feature.UseBigDecimal.getMask();

    }

    @Test
    @DisplayName("测试整数序列化与反序列化")
    public void testInteger() {
        ThirdUpMsgModel model = new ThirdUpMsgModel();

        // 整形数据序列化后没有小数点，反序列化后是整形
        short s = 1;
        model.setData(s);
        String jsonString = JSONObject.toJSONString(model);
        ThirdUpMsgModel model1 = JSONObject.parseObject(jsonString, ThirdUpMsgModel.class);
        Object intData = ThirdUpMsgModel.getData(EncodeTypeEnum.TYPE_INT, model1.getData());


        int i = 1;
        model.setData(i);
        jsonString = JSONObject.toJSONString(model);
        model1 = JSONObject.parseObject(jsonString, ThirdUpMsgModel.class);
        intData = ThirdUpMsgModel.getData(EncodeTypeEnum.TYPE_INT, model1.getData());

        long l = 1;
        model.setData(l);
        jsonString = JSONObject.toJSONString(model);
        model1 = JSONObject.parseObject(jsonString, ThirdUpMsgModel.class);
       Object longData = ThirdUpMsgModel.getData(EncodeTypeEnum.TYPE_LONG, model1.getData());
        assert longData instanceof Long : "false";

        long l1 = System.currentTimeMillis();
        assert l1 > Integer.MAX_VALUE : "false";
        model.setData(l1);
        jsonString = JSONObject.toJSONString(model);
        model1 = JSONObject.parseObject(jsonString, ThirdUpMsgModel.class);
         longData = ThirdUpMsgModel.getData(EncodeTypeEnum.TYPE_LONG, model1.getData());
        assert longData instanceof Long : "false";
    }

}
