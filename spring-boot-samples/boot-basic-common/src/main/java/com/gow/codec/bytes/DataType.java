package com.gow.codec.bytes;

import com.gow.codec.Charsets;
import com.gow.codec.CodecUtils;
import com.gow.codec.bytes.deserializable.DecodeContext;
import com.gow.codec.bytes.deserializable.Deserializer;
import com.gow.codec.bytes.serializable.ObjectField;
import com.gow.codec.bytes.serializable.Serializer;
import com.gow.codec.exception.DecodeException;
import com.gow.codec.exception.EncodeException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据类型
 * wcc 2022/5/5
 */
@Slf4j
public enum DataType implements Serializer, Deserializer {
    BOOLEAN("boolean") {
        @Override
        public byte[] serialize(Object obj) {
            return new byte[]{(Boolean) obj ? (byte) 1 : (byte) 0};
        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {
            return DecodeContext.builder().obj(bytes[offset] == 0)
                    .offset(offset + 1)
                    .build();
        }
    },
    BYTE("byte") {
        @Override
        public byte[] serialize(Object obj) {
            Byte b = (Byte) obj;
            return new byte[]{b};
        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {
            return DecodeContext.builder().obj(bytes[offset])
                    .offset(offset + 1)
                    .build();
        }
    },
    SHORT("short") {
        @Override
        public byte[] serialize(Object obj) {
            Short s = (Short) obj;
            return BytesUtils.shortToBe(s);
        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {
            return DecodeContext.builder().obj((short) BytesUtils.beToInt(bytes, offset, 2))
                    .offset(offset + 2)
                    .build();
        }
    },
    INT("int") {
        @Override
        public byte[] serialize(Object obj) {
            Integer i = (Integer) obj;
            return BytesUtils.intToBe(i);
        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {

            return DecodeContext.builder().obj(BytesUtils.beToInt(bytes, offset, 4))
                    .offset(offset + 4)
                    .build();
        }
    },
    LONG("long") {
        @Override
        public byte[] serialize(Object obj) {
            Long i = (Long) obj;
            return BytesUtils.longToBe(i);
        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {

            return DecodeContext.builder().obj(BytesUtils.beToInt(bytes, offset, 8))
                    .offset(offset + 8)
                    .build();
        }
    },
    FLOAT("float") {
        @Override
        public byte[] serialize(Object obj) {
            float f = (Float) obj;
            return BytesUtils.floatToBe(f);
        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {

            return DecodeContext.builder().obj(BytesUtils.beToFloat(bytes, offset, 4))
                    .offset(offset + 4)
                    .build();
        }
    },
    DOUBLE("double") {
        @Override
        public byte[] serialize(Object obj) {
            float f = (Float) obj;
            return BytesUtils.doubleToBe(f);
        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {

            return DecodeContext.builder().obj(BytesUtils.beToDouble(bytes, offset, 8))
                    .offset(offset + 8)
                    .build();
        }
    },
    ENUM("enum") {
        @Override
        public byte[] serialize(Object obj) {

            return OBJECT.serialize(obj);
        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {
            // proxy 实现
            return null;
        }
    },
    // length 序列化到字节数据中
    STRING("varStr-utf-8") {
        @Override
        public byte[] serialize(Object obj) {
            String s = (String) obj;
            //
            byte[] bytes = CodecUtils.getVariableNumberBytes(s.length());
            byte[] result = new byte[bytes.length + s.length()];
            System.arraycopy(bytes, 0, result, 0, bytes.length);
            byte[] sBytes = s.getBytes();
            System.arraycopy(sBytes, 0, result, bytes.length, sBytes.length);
            return result;
        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {
            DecodeContext context = VARINT.deserialize(bytes, offset, 0, null);
            int decodeLen = (int) context.getObj();
            byte[] strings = new byte[decodeLen];

            System.arraycopy(bytes, context.getOffset(), strings, 0, decodeLen);
            return DecodeContext.builder().obj(new String(strings))
                    .offset(context.getOffset() + decodeLen)
                    .build();
        }
    },
    // 注解中指定长度length()
    ASCII_LEN("ascii_len") {
        @Override
        public byte[] serialize(Object obj) {
            String s = (String) obj;
            return s.getBytes();
        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {
            byte[] strings = new byte[length];
            System.arraycopy(bytes, offset, strings, 0, length);
            return DecodeContext.builder().obj(new String(strings))
                    .offset(offset + length)
                    .build();
        }
    },
    // 注解中指定长度length()
    GBK_LEN("gbk_len") {
        @Override
        public byte[] serialize(Object obj) {
            String s = (String) obj;
            return s.getBytes(Charsets.GBK);
        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {
            byte[] strings = new byte[length];
            System.arraycopy(bytes, offset, strings, 0, length);
            return DecodeContext.builder().obj(new String(strings, Charsets.GBK))
                    .offset(offset + length)
                    .build();
        }
    },

    OBJECT("object") {
        @SneakyThrows
        @Override
        public byte[] serialize(Object obj) {
            Class<?> aClass = obj.getClass();
            List<Byte> list = new ArrayList<>();
            // 获取全量的
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field : declaredFields) {
                ObjectField annotation = field.getAnnotation(ObjectField.class);
                if (annotation != null) {
                    // 设置field 可获取
                    field.setAccessible(true);
                    Serializer serializer = annotation.dataType();
                    int loop = annotation.loop();
                    Object fValue = field.get(obj);
                    if (fValue instanceof List) {
                        ((List<?>) fValue).forEach(o -> {
                            doSerialize(list, serializer, o);
                        });
                    } else {
                        doSerialize(list, serializer, fValue);
                    }
                }
            }
            byte[] bytes = new byte[list.size()];
            for (int i = 0; i < list.size(); i++) {
                bytes[i] = list.get(i);
            }
            return bytes;
        }

        private void doSerialize(List<Byte> list, Serializer serializer, Object fValue) {
            byte[] bytes = serializer.serialize(fValue);
            for (byte aByte : bytes) {
                list.add(aByte);
            }
        }

        @SneakyThrows
        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {
            Class<?> aClass = obj.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);

                ObjectField annotation = field.getAnnotation(ObjectField.class);
                if (annotation == null) {
                    continue;
                }
                DataType dataType = annotation.dataType();
                length = annotation.length();
                if (annotation.dataType() == ENUM) {
                    log.info("enum not support");
                } else if (field.getType().isArray()) {
                    // 数组类型支持
                    DecodeContext context = decodeArray(bytes, offset, length, obj, field.getType(), annotation);
                    field.set(obj, context.getObj());
                    offset = context.getOffset();
                } else if (field.getType() == List.class) {
                    // 当前集合的泛型类型
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        // 得到泛型里的class类型对象
                        Class<?> actualType = (Class<?>) pt.getActualTypeArguments()[0];
                        List<Object> list = new ArrayList<>();
                        Field loopField = aClass.getDeclaredField(annotation.loopFieldName());
                        loopField.setAccessible(true);
                        DecodeContext context = decodeLength(bytes, offset, obj, annotation);
                        int loop = (int) context.getObj();
                        offset = context.getOffset();
                        int i = 0;
                        while (i++ < loop) {
                            // 解析泛型对象
                            Object instance = actualType.getDeclaredConstructor().newInstance();
                            DecodeContext ctx = getDataType(actualType, annotation).deserialize(bytes, offset, length, instance);
                            list.add(ctx.getObj());
                            offset = ctx.getOffset();
                        }
                        // 设置泛型数据
                        field.set(obj, list);
                    } else {
                        log.info("occur not support genericType:{}", field);
                    }
                } else {

                    DecodeContext context = dataType.deserialize(bytes, offset, length, DataType.getByType(dataType, field, aClass, annotation, obj));
                    field.set(obj, context.getObj());
                    offset = context.getOffset();
                }
            }
            return DecodeContext.builder().obj(obj)
                    .offset(offset).build();
        }

        @SneakyThrows
        private DecodeContext decodeArray(byte[] bytes, int offset, int length, Object obj, Class<?> aClass, ObjectField annotation) {

            DecodeContext context = decodeLength(bytes, offset, obj, annotation);
            // 此处可以抽象为
            int loop = (int) context.getObj();
            offset = context.getOffset();
            Object newInstance = Array.newInstance(aClass.getComponentType(), loop);
            if (loop > 0) {
                Class<?> componentType = newInstance.getClass().getComponentType();
                for (int i = 0; i < loop; i++) {
                    Object component = null;
                    if (!ignore_type.contains(componentType)) {
                        component = componentType.getDeclaredConstructor().newInstance();
                    }
                    DecodeContext ctx = getDataType(componentType, annotation).deserialize(bytes, offset, length, component);
                    Array.set(newInstance, i, ctx.getObj());
                    offset = ctx.getOffset();
                }
            }
            return DecodeContext.builder().obj(newInstance)
                    .offset(offset)
                    .build();
        }
    },
    VARINT("varInt") {
        @Override
        public byte[] serialize(Object obj) {
            Integer i = (Integer) obj;
            return CodecUtils.getVariableNumberBytes(i);

        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {
            int len = 0;
            // 计算length bytes 长度
            while (bytes[offset + len] <= 0) {
                len++;
            }
            // 可以做len check
            byte[] lenBytes = new byte[len];
            System.arraycopy(bytes, offset, lenBytes, 0, len);
            return DecodeContext.builder()
                    .obj(CodecUtils.decode(lenBytes))
                    .offset(offset + len).build();
        }
    },
    VARARRAY("varArray") {
        @Override
        public byte[] serialize(Object obj) {
            // 可参考OBJECT 中的序列化数组和List
            throw new EncodeException("not support");
        }

        @Override
        public DecodeContext deserialize(byte[] bytes, int offset, int length, Object obj) {
            // 可参考OBJECT 中的反序列化数组和List
            throw new DecodeException("not support");
        }
    },
    ;

    private static DecodeContext decodeLength(byte[] bytes, int offset, Object obj, ObjectField annotation) throws NoSuchFieldException, IllegalAccessException {
        int loop;
        String loopFieldName = annotation.loopFieldName();
        if (StringUtils.isBlank(loopFieldName)) {
            loop = annotation.loop();
            if (loop < 0) {
                // 变长数组
                int len = 0;
                // 计算length bytes 长度
                while (bytes[offset + len] <= 0) {
                    len++;
                }
                // 可以做len check
                byte[] lenBytes = new byte[len];
                System.arraycopy(bytes, offset, lenBytes, 0, len);
                loop = CodecUtils.decode(lenBytes);
                offset += len;
            }
        } else {
            Field loopField = obj.getClass().getDeclaredField(loopFieldName);
            loopField.setAccessible(true);
            loop = ((Number) loopField.get(obj)).intValue();
        }
        return DecodeContext.builder().obj(loop)
                .offset(offset)
                .build();
    }

    private static final Set<Class<?>> ignore_type;

    static {
        ignore_type = new HashSet<>();
        ignore_type.add(Short.class);
        ignore_type.add(Integer.class);
        ignore_type.add(Long.class);
    }

    public static Deserializer getDataType(Class<?> actualType, ObjectField annotation) {
        if (Boolean.class.equals(actualType)) {
            return BOOLEAN;
        } else if (Byte.class.equals(actualType)) {
            return BYTE;
        } else if (Short.class.equals(actualType)) {
            return SHORT;
        } else if (Integer.class.equals(actualType)) {
            return INT;
        } else if (Long.class.equals(actualType)) {
            return LONG;
        } else if (Float.class.equals(actualType)) {
            return FLOAT;
        } else if (Double.class.equals(actualType)) {
            return DOUBLE;
        } else if (String.class.equals(actualType)) {
            return annotation.dataType();
        } else {
            return annotation.dataType();
        }
    }

    DataType(String type) {
        this.type = type;
    }

    private String type;

    @SneakyThrows
    public static Object getByType(DataType type, Field field, Class<?> aClass, ObjectField annotation, Object obj) {
        switch (type) {
            case BYTE:
                return (byte) 1;
            case INT:
                return 1;
            case SHORT:
                return (short) 1;
            case ASCII_LEN:
            case GBK_LEN:
            case STRING:
                return "";
            case FLOAT:
            case DOUBLE:
                return 0;
            case OBJECT:
                if (!annotation.classMethod().equals("")) {
                    Method method = aClass.getMethod(annotation.classMethod());
                    Class<?> zClass = (Class<?>) method.invoke(obj);
                    return zClass.getDeclaredConstructor().newInstance();
                }
                return field.getType().getDeclaredConstructor().newInstance();

        }
        return null;
    }
}
