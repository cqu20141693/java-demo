package com.gow.graphql.project;

import lombok.Data;

/**
 * @author gow
 * @date 2021/7/11 0011
 */
@Data
public class Release {

    private String version;

    private ReleaseStatus status;

    private String referenceDocUrl;

    private String apiDocUrl;

    private boolean current;

}
