package com.gow.graphql.project;

import java.util.List;
import lombok.Data;

/**
 * @author gow
 * @date 2021/7/11 0011
 */
@Data
public class Project {
    private String slug;

    private String name;

    private String repositoryUrl;

    private ProjectStatus status;

    private List<Release> releases;
}
