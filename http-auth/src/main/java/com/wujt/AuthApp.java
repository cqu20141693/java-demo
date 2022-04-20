package com.wujt;

import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wujt
 */
public class AuthApp {
    public static final MediaType JSON
            = MediaType.parse("application/json");
    private static OkHttpClient client = new OkHttpClient();


    public static void main(String[] args) throws Exception {

        String gatewayUrl = "http://121.196.121.156:80/api/v1";
        String HC_USER_KEY = "HC-USER-KEY";
        String HC_USER_AUTH_KEY = "HC-USER-AUTH-KEY";


        String userKey = "CQcuCPsJ";
        String authKey = "jxeW3sDY";
        String secretKey = "hDu5Koa032q6qPTV";

        String deviceKey = "f30fbf10c51548869c0f636ed0c15fca";
        String dataType = "STRING";
        String dataName = "string-test";
        String body = "test-data";


        long ts = System.currentTimeMillis();

        String nonce = SignTools.randomNonce(16);

        String signature = getString(secretKey, body, ts, nonce);


        String url = gatewayUrl + "/devices/" +
                deviceKey +
                "/datastreams/" +
                dataName +
                "/data" +
                "?" +
                "dataType=3&" +
                "ts=" + ts +"&"+
                "nonce=" + nonce +"&"+
                "signature=" + signature;

        RequestBody requestBody = RequestBody.create(JSON, body);
        Request request = new Request.Builder()
                .url(url)
                .header(HC_USER_KEY, userKey)
                .header(HC_USER_AUTH_KEY, authKey)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getString(String secretKey, String body, long ts, String nonce) throws Exception {
        List<String> paramStrs = new ArrayList<String>(3);
        paramStrs.add("ts=" + ts);
        paramStrs.add("nonce=" + nonce);
        paramStrs.add("dataType=3");

        Collections.sort(paramStrs);
        StringBuilder signStrBuilder = new StringBuilder();
        boolean isFirstLoop = true;
        for (String paramStr : paramStrs) {
            if (isFirstLoop) {
                isFirstLoop = false;
            } else {
                signStrBuilder.append('&');
            }
            signStrBuilder.append(paramStr);
        }
        signStrBuilder.append(body);
        String signature = SignTools.signWithHmacsh1(secretKey, signStrBuilder.toString());
        if (signature == null || signature.isEmpty()) {
            throw new Exception("signature is null");
        }
        return signature;
    }
}
