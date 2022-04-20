package com.gow.jackson.domain.rename;

import static com.gow.jackson.JacksonObject.toDoc;
import static com.gow.jackson.JacksonObject.toJson;
import static com.gow.jackson.JacksonObject.toObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gow.jackson.domain.rename.User;
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
        User object = new User("John Doe", "external", "user", "code");
        String json = toJson(object);
        System.out.println(json);
        DocumentContext doc = toDoc(json);
        assertEquals("John Doe", doc.read("$.name", String.class));
        assertEquals("external", doc.read("$.externalCode", String.class));
        assertEquals("user", doc.read("$.publicCode", String.class));
        assertEquals("code", doc.read("$.internalCode", String.class));
    }

    public static void jsonToObject() throws IOException {
        String json = "{\"name\":\"John Doe\",\"code\":\"external\",\"userCode\":\"user\",\"internalCode\":\"code\"}";
        User object = toObject(json, User.class);
        assertEquals("John Doe", object.getName());
        assertEquals("external", object.getExternalCode());
        assertEquals("user", object.getUserCode());
        assertEquals("code", object.getCode());
    }
}
