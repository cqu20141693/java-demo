package com.gow.jackson.jsonnode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gow.jackson.domain.simple.Person;

/**
 * @author gow
 * @date 2021/7/24
 */
public class JsonNodeTest {
    public static void main(String[] args) throws JsonProcessingException {
        Person person = new Person("gow");
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(person);
        JsonNode jsonNode = objectMapper.readValue(s, JsonNode.class);
        String name = jsonNode.get("name").asText();
        System.out.println(name);
    }
}
