package com.gow.jackson.domain.oneway;

import static com.gow.jackson.JacksonObject.toDoc;
import static com.gow.jackson.JacksonObject.toJson;
import static com.gow.jackson.JacksonObject.toObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gow.jackson.domain.oneway.User;
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
        User object = new User("John Doe", "secret", 100);
        String json = toJson(object);
        System.out.println("User:" + json);
        DocumentContext doc = toDoc(json);
        assertEquals("John Doe", doc.read("$.name", String.class));
        assertEquals(100, doc.read("$.identifier", Integer.class));
        assertEquals(null, doc.read("$.password", Integer.class));
    }

    public static void jsonToObject() throws IOException {
        String json = "{\"name\":\"John Doe\",\"identifier\":100,\"password\":\"secret\"}";
        User object = toObject(json, User.class);
        assertEquals("John Doe", object.getName());
        assertEquals(null, object.getIdentifier());
        assertEquals("secret", object.getPassword());
    }
}
