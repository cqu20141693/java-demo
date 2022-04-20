package com.cc.core.utils;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.core.geo.GeoJson;
import org.springframework.data.elasticsearch.core.geo.GeoJsonLineString;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPoint;

public class EsUtils {
    private static final String TYPE_KEY = "type";
    private static final String LAT_KEY = "lat";
    private static final String LON_KEY = "lon";

    public static RestClient getLowClient(RestHighLevelClient highClient) {
        return highClient.getLowLevelClient();
    }
}
