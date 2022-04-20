package com.cc.core.index;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Data
public class Author {
    @Field(type = Text)
    private String name;
}
