package com.gow.jackson.domain.ingore;

import static com.gow.jackson.JacksonObject.toDoc;
import static com.gow.jackson.JacksonObject.toJson;
import static com.gow.jackson.JacksonObject.toObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gow.jackson.domain.ingore.Friend;
import com.jayway.jsonpath.DocumentContext;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/7/24
 */
public class FriendTest {

    @Test
    @DisplayName("Ignore field conversion object to json")
    public void objectToJson() throws JsonProcessingException {
        Friend object = new Friend("John Doe", "secret");
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("John Doe", doc.read("$.name", String.class));
        assertEquals(null, doc.read("$.secret", String.class));
    }

    @Test
    @DisplayName("Ignore field conversion json to object")
    public void jsonToObject() throws IOException {
        String json = "{\"name\":\"John Doe\",\"secret\":\"secret\"}";
        Friend object = toObject(json, Friend.class);
        assertEquals("John Doe", object.getName());
        assertEquals(null, object.getSecret());
    }
}
