package com.gow.graphql.repository;

import graphql.schema.idl.RuntimeWiring;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/11 0011
 */
@Component
public class ArtifactRepositoryDataWiring implements RuntimeWiringBuilderCustomizer {

    private final ArtifactRepositories repositories;

    public ArtifactRepositoryDataWiring(ArtifactRepositories repositories) {
        this.repositories = repositories;
    }

    @Override
    public void customize(RuntimeWiring.Builder builder) {
        builder.type("Query", typeWiring -> typeWiring
                .dataFetcher("artifactRepositories", env -> repositories.findAll())
                .dataFetcher("artifactRepository", env -> repositories.findOne()));
    }

}
