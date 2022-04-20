package com.gow.camera.api.lecheng.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gow.camera.api.lecheng.model.resp.ResponseResult;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.io.IOUtils;

@Slf4j
public class HttpSend {

    public static String execute(Map<String, Object> paramsMap, String method) {
        Map<String, Object> map = paramsInit(paramsMap);
        // 返回json
        return doPost(CONST.HOST + ":" + CONST.PORT + "/openapi/" + method, map);

    }

    public static String doPost(String url, Map<String, Object> map) {
        String json = JSON.toJSONString(map);
        ProtocolSocketFactory factory = new MySecureProtocolSocketFactory();
        Protocol.registerProtocol("https", new Protocol("https", factory, 443));
        HttpClient client = new HttpClient();
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        PostMethod method = new PostMethod(url);
        System.out.println(url);
        try {
            RequestEntity entity = new StringRequestEntity(json, "application/json", "UTF-8");
            method.setRequestEntity(entity);
            client.executeMethod(method);

            InputStream inputStream = method.getResponseBodyAsStream();
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    protected static Map<String, Object> paramsInit(Map<String, Object> paramsMap) {
        long time = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();
        String id = UUID.randomUUID().toString();

        StringBuilder paramString = new StringBuilder();
        paramString.append("time:").append(time).append(",");
        paramString.append("nonce:").append(nonce).append(",");
        paramString.append("appSecret:").append(CONST.SECRET);

        String sign = "";
        // 计算MD5得值
        try {
            System.out.println("传入参数：" + paramString.toString().trim());
            sign = DigestUtils.md5Hex(paramString.toString().trim().getBytes(StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> systemMap = new HashMap<>();
        systemMap.put("ver", "1.0");
        systemMap.put("sign", sign);
        systemMap.put("appId", CONST.APPID);
        systemMap.put("nonce", nonce);
        systemMap.put("time", time);

        Map<String, Object> map = new HashMap<>();
        map.put("system", systemMap);
        map.put("params", paramsMap);
        map.put("id", id);
        return map;
    }

}
