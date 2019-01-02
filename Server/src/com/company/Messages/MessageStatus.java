package com.company.Messages;

import java.io.Serializable;

public class MessageStatus implements Serializable {
    private String username;
    private String name;
    private String gender;
    private String dob;
    private String contact;
    private String qualification;
    private String college;
    private String optional;

    public MessageStatus(String Username, String Name, String Gender, String Dob, String Contact, String Qualification, String College, String Optional) {
        this.username = Username;
        this.name = Name;
        this.gender = Gender;
        this.dob = Dob;
        this.contact = Contact;
        this.qualification = Qualification;
        this.college = College;
        this.optional = Optional;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getContact() {
        return contact;
    }

    public String getQualification() {
        return qualification;
    }

    public String getCollege() {
        return college;
    }

    public String getOptional() {
        return optional;
    }
}

