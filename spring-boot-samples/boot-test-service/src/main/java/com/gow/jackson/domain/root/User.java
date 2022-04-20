package com.gow.jackson.domain.root;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * @author gow
 * @date 2021/7/24
 */

@JsonRootName("user")
public class User {
    private String name;

    private User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }
}
