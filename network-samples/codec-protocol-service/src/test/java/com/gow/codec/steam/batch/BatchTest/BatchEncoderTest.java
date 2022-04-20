package com.gow.codec.steam.batch.BatchTest;

import static com.gow.codec.model.EncodeTypeEnum.TYPE_INT;
import static com.gow.codec.model.EncodeTypeEnum.TYPE_JSON;
import static com.gow.codec.model.EncodeTypeEnum.TYPE_STRING;
import com.alibaba.fastjson.JSONObject;
import com.gow.codec.decode.batch.BatchStreamDecoder;
import com.gow.codec.encode.batch.BatchStreamEncoder;
import com.gow.codec.model.batch.BatchStreamModel;
import com.gow.codec.model.batch.SingleStreamModel;
import java.util.ArrayList;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/7/29 0029
 */
@Slf4j
public class BatchEncoderTest {

    @Test
    @DisplayName("test decode batch bytes")
    public void test() {


        byte[] batchBytes =
                new byte[]{0x00, 0x03, 0x05, 0x06, 0x6c, 0x6f, 0x72, 0x61, 0x43, 0x68, 0x01, 0x30, 0x05, 0x0a, 0x62,
                        0x61, 0x74, 0x74, 0x65, 0x72, 0x79, 0x56, 0x6f, 0x6c, 0x08, 0x39,
                        0x2e, 0x38, 0x30, 0x32, 0x32, 0x37, 0x33, 0x05, 0x08, 0x70, 0x6f, 0x77, 0x65, 0x72, 0x56, 0x6f,
                        0x6c, 0x09, 0x31, 0x36,
                        0x2e, 0x37, 0x33, 0x32, 0x30, 0x33, 0x31};

        BatchStreamModel model = decoder.decode(batchBytes);

    }


    public static BatchStreamDecoder decoder = new BatchStreamDecoder();

    public static BatchStreamEncoder encoder = new BatchStreamEncoder();

    public static void main(String[] args) {
        BatchStreamModel model = new BatchStreamModel();
        ArrayList<SingleStreamModel> streamModels = new ArrayList<>();
        model.setSelf(streamModels);
        model.setBizTime(System.currentTimeMillis());


        BatchStreamModel model2 = new BatchStreamModel();
        ArrayList<SingleStreamModel> streamModels2 = new ArrayList<>();
        model2.setSelf(streamModels2);


        SingleStreamModel jsonStreamModel = new SingleStreamModel();
        jsonStreamModel.setStream("test-json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userVersion", 1);
        jsonStreamModel.setData(jsonObject);
        jsonStreamModel.setEncodeType(TYPE_JSON.getTypeCode());
        jsonStreamModel.setBizTime(System.currentTimeMillis());

        SingleStreamModel intStreamModel = new SingleStreamModel();
        intStreamModel.setStream("test-int");
        intStreamModel.setData(1);
        intStreamModel.setEncodeType(TYPE_INT.getTypeCode());
        streamModels.add(intStreamModel);

        streamModels2.add(intStreamModel);
        streamModels2.add(jsonStreamModel);

        log.info("model={}", JSONObject.toJSONString(model));
        byte[] bytes = encoder.encode(model, (byte) 1);
        if (bytes == null) {
            log.info(" encode failed");
        }
        BatchStreamModel batchStreamModel = decoder.decode(bytes);

        log.info("model2={}", JSONObject.toJSONString(model2));
        byte[] bytes1 = encoder.encode(model2,(byte) 1);

        BatchStreamModel batchStreamMode2 = decoder.decode(bytes1);
        if (bytes1 == null) {
            log.info("encode failed");
        }

    }

    @Test
    @DisplayName("batchBytesEncode")
    public void batchBytesEncode() {
        BatchStreamModel model = new BatchStreamModel();
        ArrayList<SingleStreamModel> streamModels = new ArrayList<>();
        model.setSelf(streamModels);

        SingleStreamModel jsonStreamModel = new SingleStreamModel();
        jsonStreamModel.setStream("json-channel");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userVersion", 0);
        jsonStreamModel.setData(jsonObject);
        jsonStreamModel.setEncodeType(TYPE_JSON.getTypeCode());

        SingleStreamModel stringStreamModel = new SingleStreamModel();
        stringStreamModel.setStream("string-channel");
        stringStreamModel.setData("test-string");
        stringStreamModel.setEncodeType(TYPE_STRING.getTypeCode());

        streamModels.add(jsonStreamModel);
        streamModels.add(stringStreamModel);

        byte[] bytes = encoder.encode(model,(byte) 1);

        String encodeToString = Base64.getEncoder().encodeToString(bytes);
        System.out.println(encodeToString);

        assert bytes != null;
        BatchStreamModel streamModel = decoder.decode(bytes);
    }

    @Test
    @DisplayName("testBatchCodec")
    public void testBatchCodec() {
        BatchStreamModel model = new BatchStreamModel();
        ArrayList<SingleStreamModel> streamModels = new ArrayList<>();
        model.setSelf(streamModels);
        model.setBizTime(System.currentTimeMillis());

        SingleStreamModel jsonStreamModel = new SingleStreamModel();
        jsonStreamModel.setStream("json-channel");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userVersion", 0);
        jsonStreamModel.setData(jsonObject);
        jsonStreamModel.setEncodeType(TYPE_JSON.getTypeCode());

        SingleStreamModel stringStreamModel = new SingleStreamModel();
        stringStreamModel.setStream("string-channel");
        stringStreamModel.setData("test-string");
        stringStreamModel.setEncodeType(TYPE_STRING.getTypeCode());
        stringStreamModel.setBizTime(System.currentTimeMillis());
        streamModels.add(jsonStreamModel);
        streamModels.add(stringStreamModel);

        byte[] bytes = encoder.encode(model,(byte) 1);

        String encodeToString = Base64.getEncoder().encodeToString(bytes);
        System.out.println(encodeToString);

        assert bytes != null;
        BatchStreamModel streamModel = decoder.decode(bytes);
        Assertions.assertEquals(model.getBizTime(), streamModel.getBizTime());
    }
}
