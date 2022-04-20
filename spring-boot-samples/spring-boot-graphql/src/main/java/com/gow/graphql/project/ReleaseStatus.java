package com.gow.graphql.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

/**
 * @author gow
 * @date 2021/7/11 0011
 */
public enum ReleaseStatus {

    GENERAL_AVAILABILITY, MILESTONE, SNAPSHOT;

    @JsonCreator
    public static ReleaseStatus fromName(String name) {
        return Arrays.stream(ReleaseStatus.values())
                .filter(type -> type.name().equals(name))
                .findFirst()
                .orElse(ReleaseStatus.GENERAL_AVAILABILITY);
    }

}
