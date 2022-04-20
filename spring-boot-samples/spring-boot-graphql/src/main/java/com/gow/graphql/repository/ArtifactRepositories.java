package com.gow.graphql.repository;

import java.util.List;

/**
 * @author gow
 * @date 2021/7/11 0011
 */
public interface ArtifactRepositories {
    void saveAll(List<ArtifactRepository> repositoryList);

    List<ArtifactRepository> findAll();

    ArtifactRepository findOne();

}
