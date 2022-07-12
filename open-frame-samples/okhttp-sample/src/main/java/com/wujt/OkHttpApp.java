package com.wujt;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.LinkedList;

/**
 * @author wujt
 */
public class OkHttpApp {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder(128);
        // data/{productKey}/{deviceKey}/{deviceTopic}
        builder.append("data/");
        String productKey = "a1C2Nb9E";
        builder.append(productKey);
        builder.append("/");
        builder.append("53387c0762ad468685a4fec9a168bdbd");
        builder.append("/#");
        String dataFilter = builder.toString();
        LinkedList<String> filters = new LinkedList<>();
        filters.add(dataFilter);
        String json = JSONObject.toJSONString(filters);

        JSONObject authInfo = getAuthInfo(productKey, json);
        System.out.println(authInfo);

    }

    private static JSONObject getAuthInfo(String productKey, String filters) {
        RequestBody requestBody = RequestBody.create(JSON, filters);
        String serverAddr = "http://service-cloud-auth.dev.svc.cluster.local:9700";
        String resource = "/app/auth/createAppAuth";
        String url = serverAddr + resource + "?" + "productKey=" + productKey;
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            JSONObject.parseObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String urlQuery="http://192.168.96.168:8840/device/instance/_query";
        RequestBody body = RequestBody.create(JSON, "{\"pageSize\":15,\"currentPage\":0,\"sorts\":[{\"name\":\"createTime\",\"order\":\"desc\"},{\"name\":\"id\",\"order\":\"desc\"}],\"terms\":[{\"column\":\"productId\",\"value\":\"1536542790799953920\",\"type\":\"and\",\"termType\":\"eq\"}]}");

        Request instance = new Request.Builder()
                .post(body)
                .addHeader("Authorization", "mes2")
                .url(urlQuery).build();
        try (Response response = client.newCall(instance).execute()) {
            String result = response.body().string();
            System.out.println(result);
            JSONObject jsonObject = JSONObject.parseObject(result);

            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
