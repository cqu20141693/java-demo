package com.cc.core.config;

import com.cc.core.geo.GeoJsonCircle;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.geo.Point;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "cc.elasticsearch", name = "uris")
@EnableConfigurationProperties(value = {ElasticsearchProperties.class})
public class ElasticsearchConfig extends ElasticsearchConfigurationSupport {


    @Bean
    @ConditionalOnMissingBean
    RestClientBuilder elasticsearchRestClientBuilder(ElasticsearchProperties properties,
                                                     ObjectProvider<RestClientBuilderCustomizer> builderCustomizers) {
        HttpHost[] hosts = properties.getUris().stream().map(this::createHttpHost).toArray(HttpHost[]::new);
        RestClientBuilder builder = RestClient.builder(hosts);
        builder.setHttpClientConfigCallback((httpClientBuilder) -> {
            builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(httpClientBuilder));
            return httpClientBuilder;
        });
        builder.setRequestConfigCallback((requestConfigBuilder) -> {
            builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(requestConfigBuilder));
            return requestConfigBuilder;
        });
        builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder;
    }

    @Bean(destroyMethod = "close")
    public RestHighLevelClient elasticsearchClient(RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }

    @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
    public ElasticsearchRestTemplate elasticsearchOperations(ElasticsearchConverter elasticsearchConverter, RestHighLevelClient elasticsearchClient) {
        return new ElasticsearchRestTemplate(elasticsearchClient, elasticsearchConverter);
    }

    private HttpHost createHttpHost(String uri) {
        try {
            return createHttpHost(URI.create(uri));
        } catch (IllegalArgumentException ex) {
            return HttpHost.create(uri);
        }
    }

    private HttpHost createHttpHost(URI uri) {
        if (!StringUtils.hasLength(uri.getUserInfo())) {
            return HttpHost.create(uri.toString());
        }
        try {
            return HttpHost.create(new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(),
                    uri.getQuery(), uri.getFragment()).toString());
        } catch (URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    @Bean
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(
                Arrays.asList(new GeoJsonCircleToMap(), new MapToGeoJsonCircle()));
    }

    /**
     * 自定义类型convert
     */
    @WritingConverter
    static class GeoJsonCircleToMap implements Converter<GeoJsonCircle, Map<String, Object>> {

        @Override
        public Map<String, Object> convert(GeoJsonCircle source) {

            LinkedHashMap<String, Object> target = new LinkedHashMap<>();
            target.put("type", source.getType());
            target.put("radius", source.getRadius());
            target.put("coordinates", source.getCoordinates());

            return target;
        }
    }

    @ReadingConverter
    static class MapToGeoJsonCircle implements Converter<Map<String, Object>, GeoJsonCircle> {

        @Override
        public GeoJsonCircle convert(Map<String, Object> source) {
            Object object = source.get("type");
            Assert.notNull(object, "Document to convert does not contain a type");
            Assert.isTrue(object instanceof String, "type must be a String");
            String type = object.toString().toLowerCase();
            Assert.isTrue(type.equalsIgnoreCase(GeoJsonCircle.TYPE), "does not contain a type 'Circle'");

            Object coordinates = source.get("coordinates");
            Assert.notNull(coordinates, "Document to convert does not contain coordinates");
            Assert.isTrue(coordinates instanceof List, "coordinates must be a List of Numbers");
            // noinspection unchecked
            List<Number> numbers = (List<Number>) coordinates;
            Assert.isTrue(numbers.size() >= 2, "coordinates must have at least 2 elements");

            Object radius = source.get("radius");
            Assert.notNull(radius, "Document to convert does not contain a type");
            Assert.isTrue(radius instanceof String, "radius must be a String");

            return new GeoJsonCircle(new Point(numbers.get(0).doubleValue(), numbers.get(1).doubleValue()), radius.toString());
        }
    }
}
