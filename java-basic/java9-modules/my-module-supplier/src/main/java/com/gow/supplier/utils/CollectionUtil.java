package com.gow.supplier.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author gow 2021/06/12
 */
public class CollectionUtil {
    /**
     * 判断collection是否为空
     * @param collection 集合
     * @return boolean
     */
    public static boolean isEmpty(Collection<?> collection){
        return collection == null || collection.isEmpty();
    }


    /**
     * 判断Collection是否非空
     * @param collection 集合
     * @return boolean
     */
    public static boolean isNotEmpty(Collection<?> collection){
        return ! isEmpty(collection);
    }

    /**
     * 判断map是否为空
     * @param map Map
     * @return boolean
     */
    public static boolean isEmpty(Map<?,?> map){
        return map == null || map.isEmpty();
    }

    /**
     * 判断map是否非
     * @param map Map
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?,?> map){
        return ! isEmpty(map);
    }
}
