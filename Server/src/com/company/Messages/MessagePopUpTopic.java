package com.company.Messages;

import java.io.Serializable;

public class MessagePopUpTopic implements Serializable {
    private String username;
    private String subject;
    private String topic;
    private String date;
    private String noOfSingleCorrect;
    private String singleTimeLimit;
    private String noOfMultipleCorrect;
    private String multipleTimeLimit;
    private String noOfTrueFalse;
    private String trueFalseTimeLimit;


    public MessagePopUpTopic(String username,String subject, String topic, String date, String noOfSingleCorrect, String singleTimeLimit, String noOfMultipleCorrect, String multipleTimeLimit, String noOfTrueFalse, String trueFalseTimeLimit) {
        this.username = username;
        this.subject = subject;
        this.topic = topic;
        this.date = date;
        this.noOfSingleCorrect = noOfSingleCorrect;
        this.singleTimeLimit = singleTimeLimit;
        this.noOfMultipleCorrect = noOfMultipleCorrect;
        this.multipleTimeLimit = multipleTimeLimit;
        this.noOfTrueFalse = noOfTrueFalse;
        this.trueFalseTimeLimit = trueFalseTimeLimit;
    }

    public void setUsername(String username) { this.username = username; }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNoOfSingleCorrect(String noOfSingleCorrect) {
        this.noOfSingleCorrect = noOfSingleCorrect;
    }

    public void setSingleTimeLimit(String singleTimeLimit) {
        this.singleTimeLimit = singleTimeLimit;
    }

    public void setNoOfMultipleCorrect(String noOfMultipleCorrect) {
        this.noOfMultipleCorrect = noOfMultipleCorrect;
    }

    public void setMultipleTimeLimit(String multipleTimeLimit) {
        this.multipleTimeLimit = multipleTimeLimit;
    }

    public void setNoOfTrueFalse(String noOfTrueFalse) {
        this.noOfTrueFalse = noOfTrueFalse;
    }

    public void setTrueFalseTimeLimit(String trueFalseTimeLimit) {
        this.trueFalseTimeLimit = trueFalseTimeLimit;
    }

    public String getUsername() { return username; }

    public String getSubject() {
        return subject;
    }

    public String getTopic() {
        return topic;
    }

    public String getDate() {
        return date;
    }

    public String getNoOfSingleCorrect() {
        return noOfSingleCorrect;
    }

    public String getSingleTimeLimit() {
        return singleTimeLimit;
    }

    public String getNoOfMultipleCorrect() {
        return noOfMultipleCorrect;
    }

    public String getMultipleTimeLimit() {
        return multipleTimeLimit;
    }

    public String getNoOfTrueFalse() {
        return noOfTrueFalse;
    }

    public String getTrueFalseTimeLimit() {
        return trueFalseTimeLimit;
    }
}
