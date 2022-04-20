package com.gow.jackson.domain.root;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gow.jackson.JacksonObject;
import com.gow.jackson.domain.root.User;
import com.jayway.jsonpath.DocumentContext;
import java.io.IOException;

/**
 * @author gow
 * @date 2021/7/24
 */
public class UserTest {

    public static void main(String[] args) throws IOException {
        objectToJson();
        jsonToObject();
    }

    public static void objectToJson() throws JsonProcessingException {
        User object = new User("John Doe");
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.WRAP_ROOT_VALUE);
        String json = objectMapper.writeValueAsString(object);
        System.out.println(json);
        DocumentContext doc = JacksonObject.toDoc(json);
        assertEquals("John Doe", doc.read("$.user.name", String.class));
    }

    public static void jsonToObject() throws IOException {
        String json = "{\"user\":{\"name\":\"John Doe\"}}";
        ObjectMapper objectMapper = new ObjectMapper().enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        User object = objectMapper.readValue(json, User.class);
        assertEquals("John Doe", object.getName());
    }
}
