package com.gow.graphql.repository;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gow
 * @date 2021/7/11 0011
 */
@Data
@Accessors(chain = true)
public class ArtifactRepository {
    private String id;

    private String name;

    private String url;

    private boolean snapshotsEnabled;

    public ArtifactRepository() {
    }

    public ArtifactRepository(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.snapshotsEnabled = true;
    }
}
