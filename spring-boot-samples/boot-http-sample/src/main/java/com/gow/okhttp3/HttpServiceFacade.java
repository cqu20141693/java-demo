package com.gow.okhttp3;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * @author gow
 * @date 2021/7/16
 */
public interface HttpServiceFacade {

    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    MediaType XML = MediaType.parse("application/xml; charset=utf-8");
    String PATH_DELIMITER = "&";
    String URI_DELIMITER = "?";
    String IP_PORT_DELIMITER = ":";
    String HTTP_PREFIX = "http://";
    String HTTPS_PREFIX = "https://";
    OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
            .build();

    /**
     * get request
     *
     * @param ip
     * @param port
     * @param path
     * @param params
     * @return
     */
    String get(String ip, Integer port, String path, Map<String, Object> params);

    /**
     * get request with headers
     *
     * @param ip
     * @param port
     * @param path
     * @param params
     * @param headers
     * @return
     */
    String getForHeader(String ip, Integer port, String path, Map<String, Object> params, Map<String, String> headers);

    /**
     * Form submission request
     *
     * @param ip
     * @param port
     * @param path
     * @param params
     * @param forms  post form parameter
     * @return
     */
    String postForm(String ip, Integer port, String path, Map<String, Object> params, Map<String, String> forms);

    /**
     * post request
     *
     * @param ip
     * @param port
     * @param path
     * @param content
     * @return
     */
    String post(String ip, Integer port, String path, Map<String, Object> params, byte[] content);

    /**
     * post request with headers
     *
     * @param ip
     * @param port
     * @param path
     * @param params
     * @param headers
     * @param content
     * @return
     */
    String postWithHeader(String ip, Integer port, String path, Map<String, Object> params, Map<String, String> headers,
                          byte[] content);

}
