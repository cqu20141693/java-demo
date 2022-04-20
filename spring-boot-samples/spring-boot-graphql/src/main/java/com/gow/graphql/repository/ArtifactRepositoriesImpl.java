package com.gow.graphql.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/11 0011
 */
@Component
public class ArtifactRepositoriesImpl implements ArtifactRepositories {

    List<ArtifactRepository> localRepository = new ArrayList<>();

    @Override
    public void saveAll(List<ArtifactRepository> repositoryList) {
        localRepository.addAll(repositoryList);
    }

    @Override
    public List<ArtifactRepository> findAll() {

        return localRepository;
    }

    @Override
    public ArtifactRepository findOne() {
        return localRepository.get(0);
    }
}
