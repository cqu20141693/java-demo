package com.gow.graphql.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

/**
 * @author gow
 * @date 2021/7/11 0011
 */
public enum ProjectStatus {
    ACTIVE, COMMUNITY, INCUBATING, ATTIC;

    @JsonCreator
    public static ProjectStatus fromName(String name) {
        return Arrays.stream(ProjectStatus.values())
                .filter(type -> type.name().equals(name))
                .findFirst()
                .orElse(ProjectStatus.ACTIVE);
    }
}
