package com.wujt;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 */
public class OkHttpApp {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();


    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        int index = 201;
        int total = 200 + index;
        CountDownLatch countDownLatch = new CountDownLatch(total);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = index; i < total; i++) {
            int finalI = i;
            executorService.submit(() -> createGateway(countDownLatch, finalI));
        }
        countDownLatch.await();
        stopWatch.stop();
        System.out.println("耗时：" + stopWatch.getTotalTimeMillis());
        executorService.shutdown();

        //aggsTest(executorService);
        //test();
    }

    private static void createGateway(CountDownLatch countDownLatch, int index) {
        JSONObject jsonObject = new JSONObject();
        String id = "ccgateway" + index;
        jsonObject.put("id", id);
        jsonObject.put("name", id);
        jsonObject.put("productId", "1551395045413605376");
        jsonObject.put("productName", "wq-test");
        jsonObject.put("version", "1551395337966309376");
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();

        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toJSONString());
        String addAddr = "http://localhost:8848/device/instance";
        Request instance = new Request.Builder()
                .post(requestBody)
                .addHeader("Authorization", "test")
                .url(addAddr).build();

        try (Response response = client.newCall(instance).execute()) {
            String result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String alarmCreate = "http://localhost:8848/device/default-alarm/1551395045413605376/" + id + "/_create";
        RequestBody nullBody = RequestBody.create(JSON, "{}");
        Request alarm = new Request.Builder().post(nullBody).addHeader("Authorization", "test")
                .url(alarmCreate).build();
        try (Response response = client.newCall(alarm).execute()) {
            String result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String deployInstance = "http://localhost:8848/device/instance/" + id + "/deploy";
        Request deploy = new Request.Builder().post(nullBody).addHeader("Authorization", "test")
                .url(deployInstance).build();
        try (Response response = client.newCall(deploy).execute()) {
            String result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();
    }

    private static void aggsTest(ExecutorService executorService) throws InterruptedException {
        int total = 5;
        CountDownLatch countDownLatch = new CountDownLatch(total);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < total; i++) {
            executorService.submit(() -> lineChart(countDownLatch));
        }

        countDownLatch.await();
        stopWatch.stop();
        System.out.println("耗时：" + stopWatch.getTotalTimeMillis());
        executorService.shutdown();
    }

    private static void lineChart(CountDownLatch countDownLatch) {
        RequestBody requestBody = RequestBody.create(JSON, "{\"aggregationStep\":\"1\",\"aggregationType\":\"3\",\"dimension\":\"3\",\"workshopCode\":\"205344210078179328,205344210329837568\",\"startDate\":\"2022-06-01\",\"endDate\":\"2022-08-30\"}");

        String serverAddr = "http://localhost:8082/api/da/day-table/lineChartData";
        Request instance = new Request.Builder()
                .post(requestBody)
                .addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICItVTY0TkdTeEdoN041eVFCUUNLeGZ6MHJQS2ZKcnU4VVExVFFmaUZfRDNnIn0.eyJleHAiOjE2NjE4OTMxOTEsImlhdCI6MTY2MTg1NzE5MiwiYXV0aF90aW1lIjoxNjYxODU3MTkxLCJqdGkiOiIwNGVhYjI0NC01NTZiLTQ5YWEtOTVhMy1kMmYyNThkN2M1MjMiLCJpc3MiOiJodHRwczovL2tjLXdlLXRlc3QuZ2VlZ2EuY29tL3JlYWxtcy9ndWMtdGVzdCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJmODVjNmEyOC02N2Q0LTQxNTktYTVhOS1kOWI2NjQ4ODZlNTQiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJndWMiLCJzZXNzaW9uX3N0YXRlIjoiNjU1NjU2MWMtZmIyYi00YzVlLThjYTUtMTk1MTIxNDM2MDQ3IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLWd1Yy10ZXN0Iiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJzaWQiOiI2NTU2NTYxYy1mYjJiLTRjNWUtOGNhNS0xOTUxMjE0MzYwNDciLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJHVUPotoXnrqEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbiIsImdpdmVuX25hbWUiOiJHVUPotoXnrqEifQ.DIyBu1bK-wn4mctRNZug2MJrcbjS3V7AUZqMPUN6I4pXXsnY6GYfwuoiJUcK2ysoeK168xaSwGXs93ylEmY8DDVPmvPDAEbxB0bQI8kZdaKUFQ3lP6XMjEyixsZfdrfmcVyvViiEieGsCjCq9o7437hTegUV_BqX-b0cJvFA9P5TQjcWrf0wm7GgmHqTI2Jc7wOnWnBBd1TEWIYeKb2hJHkyyQVF9R7WrzbTPnEmZRza47maxRm3JvwC_rIFHaQfbOzVK-NBNKbi4jRRZfaj6RsgdemSbFwnZn8nSJ2wqSGilVU0zZzOP1I1pfJtjHxMuv_3f1KuQCglOffWZQUL3g")
                .url(serverAddr).build();
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        try (Response response = client.newCall(instance).execute()) {
            String result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();
    }

    private static void test() {
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


        String urlQuery = "http://192.168.96.168:8840/device/instance/_query";
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
