package com.gow.graphql.greeting;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.graphql.boot.RuntimeWiringBuilderCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author gow
 * @date 2021/7/11 0011
 *
 * Servlet Filter that adds a Servlet request attribute.
 */
@Component
public class GreetingDataWiring implements RuntimeWiringBuilderCustomizer {

    @Override
    public void customize(RuntimeWiring.Builder builder) {
        builder.type("Query", typeWiring -> typeWiring.dataFetcher("greeting", env -> {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            return "Hello " + attributes.getAttribute(RequestAttributeFilter.NAME_ATTRIBUTE, SCOPE_REQUEST);
        }));
    }

}