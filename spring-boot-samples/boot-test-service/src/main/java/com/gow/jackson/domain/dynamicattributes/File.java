package com.gow.jackson.domain.dynamicattributes;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gow
 * @date 2021/7/24
 */
public class File {

    private String name;
    private Map<String, String> attrs;

    private File() {
        this.attrs = new HashMap<>();
    }

    public File(String name, Map<String, String> attrs) {
        this.name = name;
        this.attrs = attrs;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    @JsonAnyGetter
    public Map<String, String> getAttrs() {
        return attrs;
    }

    private void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    @JsonAnySetter
    public void addAttr(String attr, String value) {
        this.attrs.put(attr, value);
    }

    public String getAttr(String attr) {
        return this.attrs.get(attr);
    }
}