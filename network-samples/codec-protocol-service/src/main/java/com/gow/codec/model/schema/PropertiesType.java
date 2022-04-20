package com.gow.codec.model.schema;

/**
 * @author gow
 * @date 2021/9/22
 */
public enum PropertiesType {
    NONE("none", (byte) -1),
    SELF_DEFINE("other", (byte) 0),
    JSON("json", (byte) 1),
    ;

    private String propertiesType;

    public byte getCode() {
        return code;
    }

    private byte code;

    private PropertiesType(String propertiesType, byte code) {

        this.propertiesType = propertiesType;
        this.code = code;
    }

    public String getPropertiesType() {
        return this.propertiesType;
    }

}
