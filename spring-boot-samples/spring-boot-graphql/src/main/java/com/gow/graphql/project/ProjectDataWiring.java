package com.gow.graphql.project;

import com.gow.graphql.repository.RuntimeWiringBuilderCustomizer;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/11 0011
 */
@Component
public class ProjectDataWiring implements RuntimeWiringBuilderCustomizer {

    private final SpringProjectsClient client;

    public ProjectDataWiring(SpringProjectsClient client) {
        this.client = client;
    }

    @Override
    public void customize(RuntimeWiring.Builder builder) {
        builder.type("Query", typeWiring -> typeWiring.dataFetcher("project", env -> {
            String slug = env.getArgument("slug");
            return client.fetchProject(slug);
        })).type("Project", typeWiring -> typeWiring.dataFetcher("releases", env -> {
            Project project = env.getSource();
            return client.fetchProjectReleases(project.getSlug());
        }));
    }

}
