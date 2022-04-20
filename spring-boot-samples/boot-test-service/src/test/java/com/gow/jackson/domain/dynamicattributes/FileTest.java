package com.gow.jackson.domain.dynamicattributes;

import static com.gow.jackson.JacksonObject.toDoc;
import static com.gow.jackson.JacksonObject.toJson;
import static com.gow.jackson.JacksonObject.toObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gow.jackson.domain.dynamicattributes.File;
import com.jayway.jsonpath.DocumentContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/7/24
 */
public class FileTest {


    @Test
    @DisplayName("Dynamic attributes conversion object to json")
    public void objectToJson() throws JsonProcessingException {
        Map<String, String> props = new HashMap<>();
        props.put("size", "120 KB");
        props.put("width", "200 px");
        props.put("height", "300 px");
        File object = new File("pic.jpg", props);
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("pic.jpg", doc.read("$.name", String.class));
        assertEquals("120 KB", doc.read("$.size", String.class));
        assertEquals("200 px", doc.read("$.width", String.class));
        assertEquals("300 px", doc.read("$.height", String.class));
    }

    @Test
    @DisplayName("Dynamic attributes conversion json to object")
    public void jsonToObject() throws IOException {
        String json = "{\"name\":\"pic.jpg\",\"size\":\"120 KB\",\"width\":\"200 px\",\"height\":\"300 px\"}";
        File object = toObject(json, File.class);
        assertEquals("pic.jpg", object.getName());
        assertEquals("120 KB", object.getAttr("size"));
        assertEquals("200 px", object.getAttr("width"));
        assertEquals("300 px", object.getAttr("height"));
        assertEquals(3, object.getAttrs().size());
    }
}
