package com.gow.graphql.repository;

import graphql.schema.idl.RuntimeWiring;

/**
 * wcc 2022/5/27
 */
public interface RuntimeWiringBuilderCustomizer {
    void customize(RuntimeWiring.Builder builder) ;
}
