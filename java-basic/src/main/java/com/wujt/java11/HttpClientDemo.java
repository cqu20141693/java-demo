package com.wujt.java11;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * ava 11 中的新 Http Client API，提供了对 HTTP/2 等业界前沿标准的支持，同时也向下兼容 HTTP/1.1，
 * 精简而又友好的 API 接口;API 通过 CompletableFuture 提供非阻塞请求和响应语义,拥有更高的性能
 *
 * @author gow 2021/06/13
 */
public class HttpClientDemo {
    static HttpClient client2 = HttpClient.newHttpClient();

    private HttpClient client1 = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(2))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://javastack.cn"))
                .GET()
                .build();

// 同步
        HttpResponse<String> response = client2.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

// 异步
        client2.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println);


    }
}
