package com.gow.jackson.domain.simple;

import static com.gow.jackson.JacksonObject.toDoc;
import static com.gow.jackson.JacksonObject.toJson;
import static com.gow.jackson.JacksonObject.toObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gow.jackson.domain.simple.Person;
import com.jayway.jsonpath.DocumentContext;
import java.io.IOException;

/**
 * @author gow
 * @date 2021/7/24
 */
public class PeopleTest {
    public static void main(String[] args) throws IOException {
        objectToJson();
        jsonToObject();
    }

    public static void objectToJson() throws JsonProcessingException {
        Person object = new Person("John Doe");
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("John Doe", doc.read("$.name", String.class));
    }

    public static void jsonToObject() throws IOException {
        String json = "{\"name\":\"John Doe\"}";
        Person object = toObject(json, Person.class);
        assertEquals("John Doe", object.getName());
    }
}
