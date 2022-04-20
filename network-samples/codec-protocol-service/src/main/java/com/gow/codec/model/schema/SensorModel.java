package com.gow.codec.model.schema;

import static com.gow.codec.model.schema.PropertiesType.JSON;
import static com.gow.codec.model.schema.PropertiesType.SELF_DEFINE;
import lombok.Data;

/**
 * @author gow
 * @date 2021/9/22
 */
@Data
public class SensorModel {

    private String name;

    private String streamName;

    private Byte encodeType;
    /**
     * {@link PropertiesType}
     */
    private Byte prosType;

    private Object properties;

    public static Byte getPropsType(PropertiesType propertiesType) {
        if (SELF_DEFINE == propertiesType) {
            return 0;
        } else if (JSON == propertiesType) {
            return 1;
        }
        return null;
    }
}
