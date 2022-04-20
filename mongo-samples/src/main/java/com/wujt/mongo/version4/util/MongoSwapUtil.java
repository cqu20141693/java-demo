package com.wujt.mongo.version4.util;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MongoSwapUtil {

    public static <T> T mongoSwap(ChangeStreamDocument<Document> document, Class<T> tClass) throws NoSuchMethodException {

        //获取一个对象的实例
        T obj = null;
        try {
            obj = tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        //获取所有的字段fileds
        Field[] fields = tClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            String[] strings = f.toString().split("\\.");
            String filedName = strings[strings.length - 1];
            Object value;
            //从文档中获取字段的值
            if ("id".equals(filedName)) {
                value = document.getFullDocument().getObjectId("_id");
            } else {
                value = document.getFullDocument().get(filedName);
            }
            //获取对应的set方法
            try {
                Method method = tClass.getMethod("set" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1), f.getType());
                //执行set方法
                try {
                    method.invoke(obj, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }
        return obj;
    }

    public static <T> T mongoSwapCache(ChangeStreamDocument<Document> document, Class<T> tClass, String type) throws NoSuchMethodException {

        //获取一个对象的实例
        T obj = null;
        try {
            obj = tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Set<String> stringSet = new HashSet<>();
        switch (type) {
            case "insert":
                stringSet = document.getFullDocument().keySet();
                break;
            case "update":
                stringSet.add("_id");
                stringSet.addAll(document.getUpdateDescription().getUpdatedFields().keySet());
                break;
            case "delete":
                stringSet = document.getDocumentKey().keySet();
                break;
            default:
                stringSet = null;
                break;
        }
        for (String s : stringSet) {

            Field field = ReflectionUtils.findField(tClass, s);
            String filedName = field.getName();
            Object value;
            //从文档中获取字段的值
            if ("_id".equals(filedName)) {
                //value = document.getFullDocument().getObjectId("_id");
                if ("delete".equals(type)) {
                    value = document.getDocumentKey().get(filedName).toString().split("=|}")[1];
                    System.out.println("value: " + value);
                } else {
                    value = document.getFullDocument().get(filedName);
                }
                filedName = "operateId";
            } else {
                value = document.getFullDocument().get(filedName);
            }
            Method method = ReflectionUtils.findMethod(tClass, "set" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1), field.getType());
            if (method != null) {
                ReflectionUtils.invokeMethod(method, obj, value);
            }

        }


        return obj;
    }

    public static Map mongoSwapMap(ChangeStreamDocument<Document> document, String type) throws NoSuchMethodException {

        Set<String> stringSet = new HashSet<>();
        Map<String, Object> map = new HashMap<>();
        switch (type) {
            case "insert":
                stringSet = document.getFullDocument().keySet();
                break;
            case "update":
                stringSet.add("_id");
                stringSet.addAll(document.getUpdateDescription().getUpdatedFields().keySet());
                break;
            case "delete":
                stringSet = document.getDocumentKey().keySet();
                break;
            default:
                stringSet = null;
                break;
        }

        for (String s : stringSet) {
            if ("_id".equals(s)) {
                if ("delete".equals(type)) {
                    map.put("operatorId", document.getDocumentKey().get(s).toString().split("=|}")[1]);
                } else {
                    map.put("operatorId", document.getFullDocument().get(s));
                }
            } else {
                map.put(s, document.getFullDocument().get(s));
            }
        }
        return map;
    }
}
