package com.wujt.mongo.version4.util;

import com.mongodb.MongoClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/16
 */
public class MongoClientManager {
    private static Map<String, MongoClient> map = new ConcurrentHashMap<>();

    public static Map<String, MongoClient> getMap() {
        return map;
    }

    public static void setMap(String uri, MongoClient mongoClient) {
        MongoClientManager.map.put(uri, mongoClient);
    }

    public static boolean containsClient(String uri) {
        return map.keySet().contains(uri);
    }
}
