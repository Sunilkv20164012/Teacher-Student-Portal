package com.company.Messages;

import java.io.Serializable;
import java.security.Key;

public class Message implements Serializable {
    private String message;
    private String from;
    private String to;

    public Message(String message, String from, String to, Key key) {
        this.message = message;
        this.from = from;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public String getFrom() {
        return from;
    }
}

