package com.gow.codec.model;


import com.gow.codec.base.TypeBinConvert;
import com.gow.codec.base.TypeConversion;
import com.gow.codec.base.TypeDoubleConvert;
import com.gow.codec.base.TypeFloatConvert;
import com.gow.codec.base.TypeIntConvert;
import com.gow.codec.base.TypeJsonConvert;
import com.gow.codec.base.TypeLongConvert;
import com.gow.codec.base.TypeShortConvert;
import com.gow.codec.base.TypeStringConvert;
import java.util.HashMap;
import java.util.Map;

public enum EncodeTypeEnum {
    TYPE_INT("int", (byte) 1, new TypeIntConvert()),

    TYPE_LONG("long", (byte) 2, new TypeLongConvert()),

    TYPE_FLOAT("float", (byte) 3, new TypeFloatConvert()),

    TYPE_DOUBLE("double", (byte) 4, new TypeDoubleConvert()),

    TYPE_STRING("string", (byte) 5, new TypeStringConvert()),

    TYPE_JSON("json", (byte) 6, new TypeJsonConvert()),

    TYPE_BIN("bin", (byte) 7, new TypeBinConvert()),

    TYPE_SHORT("short", (byte) 8, new TypeShortConvert()),

    UNKNOWN("unknown", (byte) -1, null);

    private final static Map<String, EncodeTypeEnum> TYPE_INNER;

    private final static Map<Byte, EncodeTypeEnum> CODE_INNER;

    static {
        TYPE_INNER = new HashMap<>();
        CODE_INNER = new HashMap<>();
        for (EncodeTypeEnum encodeTypeEnum : EncodeTypeEnum.values()) {
            TYPE_INNER.put(encodeTypeEnum.typeName, encodeTypeEnum);
            CODE_INNER.put(encodeTypeEnum.typeCode, encodeTypeEnum);
        }
    }

    private final String typeName;

    private final byte typeCode;

    private final TypeConversion<?> abstractTypeConvert;

    EncodeTypeEnum(String typeName, byte typeCode, TypeConversion<?> abstractTypeConvert) {
        this.typeName = typeName;
        this.typeCode = typeCode;
        this.abstractTypeConvert = abstractTypeConvert;
    }

    public static EncodeTypeEnum parseFromType(String typeName) {
        return TYPE_INNER.getOrDefault(typeName, UNKNOWN);
    }

    public static EncodeTypeEnum parseFromCode(byte typeCode) {
        return CODE_INNER.getOrDefault(typeCode, UNKNOWN);
    }

    public String getTypeName() {
        return typeName;
    }

    public byte getTypeCode() {
        return typeCode;
    }

    public TypeConversion<?> getTypeConvert() {
        return abstractTypeConvert;
    }
}
