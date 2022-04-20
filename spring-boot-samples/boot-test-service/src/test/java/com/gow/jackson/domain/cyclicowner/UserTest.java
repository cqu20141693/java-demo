package com.gow.jackson.domain.cyclicowner;

import static com.gow.jackson.JacksonObject.toDoc;
import static com.gow.jackson.JacksonObject.toJson;
import static com.gow.jackson.JacksonObject.toObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.DocumentContext;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/7/24
 */
public class UserTest {

    @Test
    @DisplayName("Cyclic relation with owner conversion object to json")
    public void objectToJson() throws JsonProcessingException {
        User object = new User("John Doe", new User.ContactData("555 555 555"));
        String json = toJson(object);
        System.out.println("user:" + json);
        DocumentContext doc = toDoc(json);
        assertEquals("John Doe", doc.read("$.name", String.class));
        assertEquals("555 555 555", doc.read("$.contactData.phone", String.class));
        assertEquals(null, doc.read("$.contactData.user", Object.class));
    }

    @Test
    @DisplayName("Cyclic relation with owner conversion object to json by child element")
    public void objectToJsonChild() throws JsonProcessingException {
        User object = new User("John Doe", new User.ContactData("555 555 555"));
        String json = toJson(object.getContactData());
        System.out.println("ContactData:" + json);
        DocumentContext doc = toDoc(json);
        assertEquals("555 555 555", doc.read("$.phone", String.class));
        assertEquals(null, doc.read("$.user", Object.class));
    }

    @Test
    @DisplayName("Cyclic relation with owner  conversion json to object")
    public void jsonToObject() throws IOException {
        String json = "{\"name\":\"John Doe\",\"contactData\":{\"phone\":\"555 555 555\"}}";
        User object = toObject(json, User.class);
        assertEquals("John Doe", object.getName());
        assertEquals("555 555 555", object.getContactData().getPhone());
        assertEquals(object, object.getContactData().getUser());
    }

    @Test
    @DisplayName("Cyclic relation with owner conversion json to object by child element")
    public void jsonToObjectChild() throws IOException {
        String json = "{\"phone\":\"555 555 555\",\"user\":{\"name\":\"John Doe\"}}";
        User.ContactData object = toObject(json, User.ContactData.class);
        assertEquals("555 555 555", object.getPhone());
        assertEquals("John Doe", object.getUser().getName());
        assertEquals(null, object.getUser().getContactData());
    }

    @Test
    @DisplayName("Cyclic relation with owner conversion cyclicJson")
    public void jsonToObjectCyclic() throws IOException {

        String json = "{\"phone\":\"555 555 555\",\"user\":{\"name\":\"John Doe\"}}";
        User.ContactData object = toObject(json, User.ContactData.class);
        User gow = new User("gow", object);
        String cyclicJson = toJson(gow);
        User user = toObject(cyclicJson, User.class);
        assertEquals("gow", user.getName());
        assertEquals("555 555 555", user.getContactData().getUser().getContactData().getPhone());
        assertEquals(user, user.getContactData().getUser());
    }
}

