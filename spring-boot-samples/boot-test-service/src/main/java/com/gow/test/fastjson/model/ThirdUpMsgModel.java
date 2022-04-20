package com.gow.test.fastjson.model;

import com.gow.codec.model.EncodeTypeEnum;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author gow
 * @date 2021/9/23
 */
@Data
public class ThirdUpMsgModel {

    private Long timeStamp;

    private Object data;

    public static Object getData(EncodeTypeEnum encodeType, Object data) {
        if (encodeType == null) {
            return data;
        }
        if (encodeType == EncodeTypeEnum.TYPE_FLOAT) {
            return ((BigDecimal) data).floatValue();
        } else if (encodeType == EncodeTypeEnum.TYPE_DOUBLE) {
            return ((BigDecimal) data).doubleValue();
        } else if (encodeType == EncodeTypeEnum.TYPE_LONG) {
            Number number = (Number) data;
            return number.longValue();
        } else {
            return data;
        }
    }

}
