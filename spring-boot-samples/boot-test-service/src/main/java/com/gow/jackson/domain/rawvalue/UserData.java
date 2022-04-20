package com.gow.jackson.domain.rawvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author gow
 * @date 2021/7/24
 */

public class UserData {
    private String name;
    @JsonRawValue
    private String json;

    private UserData() {
    }

    public UserData(String name, String json) {
        this.name = name;
        this.json = json;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getJson() {
        return json;
    }

    private void setJson(String json) {
        this.json = json;
    }

    @JsonProperty(value = "json")
    private void setJsonRaw(JsonNode jsonNode) {
        setJson(jsonNode.toString());
    }
}