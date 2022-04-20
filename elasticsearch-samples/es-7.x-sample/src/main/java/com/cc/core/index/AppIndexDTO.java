package com.cc.core.index;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Data
@Document(indexName = "app")
@Accessors(chain = true)
public class AppIndexDTO {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String appKey;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Integer)
    private Integer status;
}
