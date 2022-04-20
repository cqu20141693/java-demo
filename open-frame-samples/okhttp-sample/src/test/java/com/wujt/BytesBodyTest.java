package com.wujt;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;

/**
 * @author wujt
 */
public class BytesBodyTest {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public void bytesBody(){

        String url="http://localhost:9093/devices/sendQos1Msg?deviceKey=53387c0762ad468685a4fec9a168bdbd&type=command&businessId=123456&topic=/ctl/123456";
        byte[] bytes=new byte[]{116, 97, 111, 103, 101};
        RequestBody body = RequestBody.create(JSON, bytes);
        Request request = new Request.Builder()
                .url(url)

                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            JSONObject object = JSONObject.parseObject(result);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
