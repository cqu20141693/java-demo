package com.gow.jackson.domain.rawvalue;

import static com.gow.jackson.JacksonObject.toDoc;
import static com.gow.jackson.JacksonObject.toJson;
import static com.gow.jackson.JacksonObject.toObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gow.jackson.domain.rawvalue.UserData;
import com.jayway.jsonpath.DocumentContext;
import java.io.IOException;

/**
 * @author gow
 * @date 2021/7/24
 */
public class UserDataTest {

    public static void main(String[] args) throws IOException {

        objectToJson();
        jsonToObject();
    }


    public static void objectToJson() throws JsonProcessingException {
        UserData object = new UserData("John Doe", "{\"data\":\"json\"}");
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("John Doe", doc.read("$.name", String.class));
        assertEquals("json", doc.read("$.json.data", String.class));
    }

    public static void jsonToObject() throws IOException {
        String json = "{\"name\":\"John Doe\",\"json\":{\"data\":\"json\"}}";
        UserData object = toObject(json, UserData.class);
        assertEquals("John Doe", object.getName());
        assertEquals("{\"data\":\"json\"}", object.getJson());
    }
}
