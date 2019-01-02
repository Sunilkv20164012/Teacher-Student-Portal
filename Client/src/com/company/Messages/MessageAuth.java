package com.company.Messages;

import java.io.Serializable;

public class MessageAuth implements Serializable {
    private String username;
    private String email;
    private String password;

    public MessageAuth(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("Username : %s\nEmail : %s\nPassword : %s", username, email, password);
    }
}

