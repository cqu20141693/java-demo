package com.cc.es.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujt
 */
@Configuration
@Slf4j
public class ESClientFactory implements FactoryBean<RestHighLevelClient>, InitializingBean, DisposableBean {

    private final static String SCHEME = "http";

    private RestHighLevelClient restHighLevelClient;

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void destroy() throws Exception {
        try {
            if (null != restHighLevelClient) {
                restHighLevelClient.close();
            }
        } catch (final Exception e) {
            log.error("Error closing ElasticSearch client: ", e);
        }
    }

    @Override
    public RestHighLevelClient getObject() throws Exception {
        return restHighLevelClient;
    }

    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        restHighLevelClient = buildClient();
    }

    private RestHighLevelClient buildClient() {
        try {
            String[] hosts = clusterNodes.split(",");
            List<HttpHost> httpHosts = new ArrayList<>(hosts.length);
            for (String node : hosts) {
                HttpHost host = new HttpHost(
                        node.split(":")[0],
                        Integer.parseInt(node.split(":")[1]),
                        SCHEME);
                httpHosts.add(host);
            }
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(httpHosts.toArray(new HttpHost[0]))
            );
        } catch (Exception e) {
            log.error("create es client error,message={}", e.getMessage());
        }
        return restHighLevelClient;
    }
}
