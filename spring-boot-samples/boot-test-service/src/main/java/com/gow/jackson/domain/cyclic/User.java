package com.gow.jackson.domain.cyclic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author gow
 * @date 2021/7/24
 */

public class User {
    private String name;
   // @JsonIgnoreProperties("user")
    private ContactData contactData;

    private User() {
    }

    public User(String name, ContactData contactData) {
        this.name = name;
        //contactData.user = this;
        this.contactData = contactData;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public ContactData getContactData() {
        return contactData;
    }

    private void setContactData(ContactData contactData) {
        this.contactData = contactData;
    }

    public static class ContactData {
        private String phone;
       // @JsonIgnoreProperties("contactData")
        private User user;

        private ContactData() {
        }

        public ContactData(String phone) {
            this.phone = phone;
        }

        public String getPhone() {
            return phone;
        }

        private void setPhone(String phone) {
            this.phone = phone;
        }

        public User getUser() {
            return user;
        }

        private void setUser(User user) {
            this.user = user;
        }
    }


}