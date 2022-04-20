package com.gow.jackson.domain.flat;

import static com.gow.jackson.JacksonObject.toDoc;
import static com.gow.jackson.JacksonObject.toJson;
import static com.gow.jackson.JacksonObject.toObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gow.jackson.domain.flat.Person;
import com.jayway.jsonpath.DocumentContext;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/7/24
 */
public class PersonTest {
    @Test
    @DisplayName("Flat json conversion object to json")
    public void objectToJson() throws JsonProcessingException {
        Person object = new Person("John Doe", new Person.Address("Lollipop Street", 101),
                new Person.Address("Soda Street", 111));
        String json = toJson(object);
        DocumentContext doc = toDoc(json);
        assertEquals("John Doe", doc.read("$.name", String.class));
        assertEquals("Lollipop Street", doc.read("$.mainAddressstreet", String.class));
        assertEquals(Integer.valueOf(101), doc.read("$.mainAddressnumber", Integer.class));
        assertEquals("Soda Street", doc.read("$.secondAddressstreet", String.class));
        assertEquals(Integer.valueOf(111), doc.read("$.secondAddressnumber", Integer.class));
    }

    @Test
    @DisplayName("Flat json conversion json to object")
    public void jsonToObject() throws IOException {
        String json =
                "{\"name\":\"John Doe\",\"mainAddressstreet\":\"Lollipop Street\",\"mainAddressnumber\":101,"
                        + "\"secondAddressstreet\":\"Soda Street\",\"secondAddressnumber\":111}";
        Person object = toObject(json, Person.class);
        assertEquals("John Doe", object.getName());
        assertEquals("Lollipop Street", object.getMainAddress().getStreet());
        assertEquals(Integer.valueOf(101), object.getMainAddress().getNumber());
        assertEquals("Soda Street", object.getSecondAddress().getStreet());
        assertEquals(Integer.valueOf(111), object.getSecondAddress().getNumber());
    }
}
