package com.gow.jackson.domain.flat;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * @author gow
 * @date 2021/7/24
 */
public class Person {

    private Person() {
    }

    public Person(String name, Address mainAddress, Address secondAddress) {
        this.name = name;
        this.mainAddress = mainAddress;
        this.secondAddress = secondAddress;
    }

    private String name;
    @JsonUnwrapped(prefix = "mainAddress")
    private Address mainAddress;
    @JsonUnwrapped(prefix = "secondAddress")
    private Address secondAddress;

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Address getMainAddress() {
        return mainAddress;
    }

    private void setMainAddress(Address mainAddress) {
        this.mainAddress = mainAddress;
    }

    public Address getSecondAddress() {
        return secondAddress;
    }

    private void setSecondAddress(Address secondAddress) {
        this.secondAddress = secondAddress;
    }

    public static class Address {
        public String street;
        private Integer number;

        private Address() {
        }

        public Address(String street, Integer number) {
            this.street = street;
            this.number = number;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }
    }
}