package com.gow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gow
 * @date 2021/7/11 0011
 *
 * 参考使用http://localhost:8080/graphiql 进行graphql 查询语法进行查询
 *
 * # {
 * #   project(slug: "spring-framework") {
 * #     repositoryUrl
 * #   }
 * # }
 * # {
 * #   project(slug: "spring-framework") {
 * #     releases {
 * #       version
 * #     }
 * #   }
 * # }
 * # {
 * #   greeting
 * # }
 * {
 * artifactRepositories {
 * id
 * name
 * url
 * snapshotsEnabled
 * }
 * }
 */
@SpringBootApplication
public class GraphqlApp {
    public static void main(String[] args) {

        SpringApplication.run(GraphqlApp.class, args);
    }
}
