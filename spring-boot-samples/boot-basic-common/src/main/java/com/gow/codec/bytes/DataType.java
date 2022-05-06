package com.gow.codec.bytes;

import com.gow.codec.bytes.deserializable.DecodeContext;
import com.gow.codec.bytes.deserializable.Deserializer;
import com.gow.codec.bytes.serializable.ObjectField;
import com.gow.codec.bytes.serializable.Serializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据类型
 * wcc 2022/5/5
 */
@Slf4j
public enum DataType implements Serializer, Deserializer {
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
    STRING("string") {
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
                DataType enumType = annotation.enumType();
                length = annotation.length();
                if (annotation.dataType() == ENUM) {
                    log.info("enum not support");
                } else if (field.getType().isArray()) {
                    // 数组类型支持
                    Field loopField = aClass.getDeclaredField(annotation.loopFieldName());
                    loopField.setAccessible(true);
                    int loop = ((Number) loopField.get(obj)).intValue();

                    if (loop > 0) {
                        Object newInstance = Array.newInstance(field.getType().getComponentType(), loop);
                        Class<?> componentType = newInstance.getClass().getComponentType();
                        for (int i = 0; i < loop; i++) {
                            Object component = componentType.getDeclaredConstructor().newInstance();
                            DecodeContext context = getDataType(componentType).deserialize(bytes, offset, length, component);
                            Array.set(newInstance, i, context.getObj());
                            offset = context.getOffset();
                        }
                        field.set(obj, newInstance);
                    }

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
                        int loop = ((Number) loopField.get(obj)).intValue();
                        int i = 0;
                        while (i++ < loop) {
                            // 解析泛型对象
                            Object instance = actualType.getDeclaredConstructor().newInstance();
                            DecodeContext context = getDataType(actualType).deserialize(bytes, offset, length, instance);
                            list.add(context.getObj());
                            offset = context.getOffset();
                        }
                        // 设置泛型数据
                        field.set(obj, list);
                    } else {
                        log.info("occur not support genericType:{}", field);
                    }
                } else {

                    DecodeContext context = dataType.deserialize(bytes, offset, length, DataType.getType(dataType, field, aClass, annotation, obj));
                    field.set(obj, context.getObj());
                    offset = context.getOffset();
                }
            }
            return DecodeContext.builder().obj(obj)
                    .offset(offset).build();
        }
    },
    ;

    public static Deserializer getDataType(Class<?> actualType) {
        if (Byte.class.equals(actualType)) {
            return BYTE;
        } else if (Short.class.equals(actualType)) {
            return SHORT;
        } else if (Integer.class.equals(actualType)) {
            return INT;
        } else if (String.class.equals(actualType)) {
            return STRING;
        } else {
            return OBJECT;
        }
    }

    DataType(String type) {
        this.type = type;
    }

    private String type;

    @SneakyThrows
    public static Object getType(DataType type, Field field, Class<?> aClass, ObjectField annotation, Object obj) {
        switch (type) {
            case BYTE:
                return (byte) 1;
            case INT:
                return 1;
            case SHORT:
                return (short) 1;
            case STRING:
                return "";
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
