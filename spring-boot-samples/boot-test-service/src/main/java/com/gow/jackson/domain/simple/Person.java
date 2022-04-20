package com.gow.jackson.domain.simple;

/**
 * @author gow
 * @date 2021/7/24
 */

public class Person {
    private String name;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }
}