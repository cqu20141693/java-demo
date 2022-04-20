package com.gow.jackson.domain.polymorphism;

import static com.gow.jackson.JacksonObject.toDoc;
import static com.gow.jackson.JacksonObject.toJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gow.jackson.domain.polymorphism.Person;
import com.jayway.jsonpath.DocumentContext;
import java.io.IOException;

/**
 * @author gow
 * @date 2021/7/24
 *
 * json object 根据类型及逆行反序列化
 */
public class PersonTest {
    public static void main(String[] args) throws IOException {

        objectToJson();
        objectToJson1();
        objectToJson2();
        objectToJson3();
        jsonToObject();
    }

    public static void objectToJson() throws JsonProcessingException {
        Person object = new Person("John Doe");
        String json = toJson(object);
        System.out.println("Person:" + json);
        DocumentContext doc = toDoc(json);
        assertEquals("person", doc.read("$.type", String.class));
        assertEquals("John Doe", doc.read("$.name", String.class));
    }

    public static void objectToJson1() throws JsonProcessingException {
        Person.Student object = new Person.Student("John Doe", "MIT");
        String json = toJson(object);
        System.out.println("Student:" + json);
        DocumentContext doc = toDoc(json);
        assertEquals("student", doc.read("$.type", String.class));
        assertEquals("John Doe", doc.read("$.name", String.class));
        assertEquals("MIT", doc.read("$.university", String.class));
    }

    public static void objectToJson2() throws JsonProcessingException {
        Person.Employee object = new Person.Employee("John Doe", "Google");
        String json = toJson(object);
        System.out.println("Employee:" + json);
        DocumentContext doc = toDoc(json);
        assertEquals("employee", doc.read("$.type", String.class));
        assertEquals("John Doe", doc.read("$.name", String.class));
        assertEquals("Google", doc.read("$.company", String.class));
    }

    public static void objectToJson3() throws JsonProcessingException {
        Person.ExchangeStudent object = new Person.ExchangeStudent("John Doe", "MIT", "Spain");
        String json = toJson(object);
        System.out.println("ExchangeStudent:" + json);
        DocumentContext doc = toDoc(json);
        assertEquals("exchangeStudent", doc.read("$.type", String.class));
        assertEquals("John Doe", doc.read("$.name", String.class));
        assertEquals("MIT", doc.read("$.university", String.class));
        assertEquals("Spain", doc.read("$.country", String.class));
    }


    public static void jsonToObject() throws IOException {
        String json =
                "{\"type\":\"exchangeStudent\",\"name\":\"John Doe\",\"university\":\"MIT\",\"country\":\"Spain\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Person object = objectMapper.readValue(json, Person.class);
        assertEquals("John Doe", object.getName());
        assertFalse(object instanceof Person.Employee);
        assertTrue(object instanceof Person.Student);
        assertTrue(object instanceof Person.ExchangeStudent);
        Person.ExchangeStudent finalObject = (Person.ExchangeStudent) object;
        assertEquals("MIT", finalObject.getUniversity());
        assertEquals("Spain", finalObject.getCountry());
    }

}
