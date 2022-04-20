package com.gow.jackson.domain.polymorphism;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author gow
 * @date 2021/7/24
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Person.Student.class, name = "student"),
        @JsonSubTypes.Type(value = Person.Employee.class, name = "employee"),
        @JsonSubTypes.Type(value = Person.class, name = "person")
})
public class Person {
    private String name;

    private Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @JsonSubTypes({
            @JsonSubTypes.Type(value = Student.class, name = "student"),
            @JsonSubTypes.Type(value = ExchangeStudent.class, name = "exchangeStudent")
    })
    public static class Student extends Person {
        private String university;

        private Student() {
        }

        public Student(String name, String university) {
            super(name);
            this.university = university;
        }

        public String getUniversity() {
            return university;
        }

        protected void setUniversity(String university) {
            this.university = university;
        }
    }

    public static class Employee extends Person {
        private String company;

        private Employee() {
        }

        public Employee(String name, String company) {
            super(name);
            this.company = company;
        }

        public String getCompany() {
            return company;
        }

        protected void setCompany(String company) {
            this.company = company;
        }
    }

    public static class ExchangeStudent extends Student {
        private String country;

        private ExchangeStudent() {
        }

        public ExchangeStudent(String name, String university, String country) {
            super(name, university);
            this.country = country;
        }

        public String getCountry() {
            return country;
        }

        protected void setCountry(String country) {
            this.country = country;
        }
    }

}