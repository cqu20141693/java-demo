package com.cc.things.metadata.types;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@SuppressWarnings("all")
public abstract class AbstractType<R> implements DataType {

    private Map<String, Object> expands;

    private String description;

    public R expands(Map<String, Object> expands) {
        if (CollectionUtils.isEmpty(expands)) {
            return (R) this;
        }
        if (this.expands == null) {
            this.expands = new HashMap<>();
        }
        this.expands.putAll(expands);
        return (R) this;
    }

    public R expand(String configKey, Object value) {

        if (value == null) {
            return (R) this;
        }
        if (expands == null) {
            expands = new HashMap<>();
        }
        expands.put(configKey, value);
        return (R) this;
    }

    public R description(String description) {
        this.description = description;
        return (R) this;
    }

}
