package com.gow.okhttp3;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/16
 */
@Slf4j
@Component
public class HttpServiceFacadeImpl implements HttpServiceFacade {

    @Override
    public String get(String ip, Integer port, String path, Map<String, Object> params) {
        String url = getURL(ip, port, path, params);
        return doGet(url, null);
    }

    @Override
    public String getForHeader(String ip, Integer port, String path, Map<String, Object> params,
                               Map<String, String> headers) {
        String url = getURL(ip, port, path, params);
        return doGet(url, headers);
    }

    @Override
    public String postForm(String ip, Integer port, String path, Map<String, Object> params,
                           Map<String, String> forms) {
        String url = getURL(ip, port, path, params);
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        Optional.ofNullable(forms).ifPresent(s -> {
            s.forEach(builder::add);
        });

        return doPost(url, builder.build(), null);
    }

    @Override
    public String post(String ip, Integer port, String path, Map<String, Object> params, byte[] content) {
        String url = getURL(ip, port, path, params);
        RequestBody requestBody = RequestBody.create(content, JSON);
        return doPost(url, requestBody, null);
    }

    @Override
    public String postWithHeader(String ip, Integer port, String path, Map<String, Object> params,
                                 Map<String, String> headers, byte[] content) {
        String url = getURL(ip, port, path, params);
        RequestBody requestBody = RequestBody.create(content, JSON);
        return doPost(url, requestBody, headers);
    }


    public String doGet(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder()
                .url(url);
        Optional.ofNullable(headers).ifPresent(h -> h.forEach(builder::addHeader));
        Request request = builder
                .build();
        return doRequest(request);
    }

    private String doPost(String url, RequestBody requestBody, Map<String, String> headers) {

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody);
        Optional.ofNullable(headers).ifPresent(h -> h.forEach(builder::addHeader));
        Request request = builder
                .build();
        return doRequest(request);
    }

    @Nullable
    private String doRequest(Request request) {
        Response response = null;
        try {
            response = client.newCall(request).execute();
            int status = response.code();
            if (response.isSuccessful()) {
                String bodyString = Objects.requireNonNull(response.body()).string();
                log.debug("bodyString={}", bodyString);
                return bodyString;
            }
        } catch (Exception e) {
            log.error("okhttp3 get exception：{}", e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    private String getURL(String ip, Integer port, String path, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder().append(HTTP_PREFIX)
                .append(ip).append(IP_PORT_DELIMITER).append(port).append(path);
        if (params != null && params.size() != 0) {
            builder.append(URI_DELIMITER);

            params.forEach((k, v) -> {
                String encodeKey = URLEncoder.encode(k, Charset.defaultCharset());
                String encodeValue = URLEncoder.encode(v.toString(), Charset.defaultCharset());
                builder.append(encodeKey).append("=").append(encodeValue).append(PATH_DELIMITER);
            });
        }
        String url = builder.toString();
        return url.substring(0, url.length() - 1);
    }
}
