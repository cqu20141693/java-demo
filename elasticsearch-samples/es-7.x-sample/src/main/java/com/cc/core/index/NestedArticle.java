package com.cc.core.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.*;
import static org.springframework.data.elasticsearch.annotations.FieldType.Nested;

@Document(indexName = "article",createIndex = true)
@Setting(shards = 1,replicas = 1,refreshInterval = "1s")
@Data
public class NestedArticle {
    @Id
    private String id;

    @MultiField(mainField = @Field(type = Text, fielddata = true), otherFields = { @InnerField(suffix = "verbatim", type = Keyword) })
    private String title;

    @Field(type = Nested, includeInParent = true)
    private List<Author> authors;

    @Field(type = Keyword)
    private String[] tags;
}
