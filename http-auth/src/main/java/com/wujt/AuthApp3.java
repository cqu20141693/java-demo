package com.wujt;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wujt
 */
public class AuthApp3 {

    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws Exception {

        String gatewayUrl = "http://121.196.121.156:80/api/v1";
        String HC_USER_KEY = "HC-USER-KEY";
        String HC_USER_AUTH_KEY = "HC-USER-AUTH-KEY";


        String userKey = "CQcuCPsJ";
        String authKey = "jxeW3sDY";
        String secretKey = "hDu5Koa032q6qPTV";

        String deviceKey = " ";
        String dataType = "STRING";
        String dataName = "string-test";
        String body = "test-data";


        long ts = System.currentTimeMillis();

        String nonce = SignTools.randomNonce(16);

        String signature = getString(secretKey, body, ts, nonce);

        while (signature.contains("+")) {
            ts = System.currentTimeMillis();
            signature = getString(secretKey, body, ts, nonce);
        }
        System.out.println(String.format("signature=%s", signature));

        String url = gatewayUrl + "/devices/" +
                deviceKey +
                "/datastreams/" +
                dataName +
                "/data" +
                "?" +
                "dataType=3&" +
                "ts=" + ts + "&" +
                "nonce=" + nonce + "&" +
                "signature=" + signature;

        System.out.println("url=" + url);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HC_USER_KEY, userKey);
        headers.set(HC_USER_AUTH_KEY, authKey);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
         // ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        stopWatch.stop();
        System.out.println("耗时：" + stopWatch.getTotalTimeMillis() + "毫秒");



        //  System.out.println(responseEntity.toString());
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
