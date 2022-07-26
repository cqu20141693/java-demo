package com.wcc.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * swagger
 * wcc 2022/7/26
 */
@Configuration
@EnableOpenApi
@Slf4j
public class SwaggerConfig {

    @Value("${server.port:8210}")
    private Integer port;

    @Bean("RestApi")
    public Docket createRestAPI() {
        return getDocket("com.wcc.scada.api", "rest api");

    }

    private Docket getDocket(String basePackage, String groupName) {
        log.info("Starting {} Swagger", groupName);
        StopWatch watch = new StopWatch();
        watch.start();
        //返回文档摘要信息
        Docket docket = new Docket(DocumentationType.OAS_30)
                .groupName(groupName)
                .apiInfo(apiInfo())
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .ignoredParameterTypes(java.sql.Date.class)
                .directModelSubstitute(LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(ZonedDateTime.class, Date.class)
                .directModelSubstitute(LocalDateTime.class, Date.class)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.basePackage(basePackage)) // 设置扫描路径
//                .paths(PathSelectors.any())  // 设置扫描所有的包
                .build()
//                .globalRequestParameters(getGlobalRequestParameters())
                .globalResponses(HttpMethod.GET, getGlobalResponseMessage())
                .globalResponses(HttpMethod.POST, getGlobalResponseMessage());
        watch.stop();
        log.info("http://localhost:{}/swagger-ui/index.html#/ {} Swagger in {} ms",port, groupName, watch.getTotalTimeMillis());
        return docket;
    }


    //生成接口信息，包括标题、联系人等
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger3接口文档")
                .description("如有疑问，请联系...")
                .contact(new Contact("gow", "https://www.wcctech.com/", "1533181183@qq.com"))
                .version("1.0")
                .build();

    }

    /**
     * 生成全局通用参数,每一个请求都会存在定义的参数
     */

    private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name("appId")
                .description("平台id")
                .required(true)
                .in(ParameterType.QUERY)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .required(false)
                .build());
        parameters.add(new RequestParameterBuilder()
                .name("deviceId")
                .description("设备的唯一id")
                .required(true)
                .in(ParameterType.QUERY)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .required(false)
                .build());
        parameters.add(new RequestParameterBuilder()
                .name("version")
                .description("客户端的版本号")
                .required(true)
                .in(ParameterType.QUERY)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .required(false)
                .build());
        return parameters;
    }

    /**
     * 生成通用响应信息
     */
    private List<Response> getGlobalResponseMessage() {
        List<Response> responseList = new ArrayList<>();
        responseList.add(new ResponseBuilder().code("404").description("找不到资源").build());
        return responseList;
    }
}
