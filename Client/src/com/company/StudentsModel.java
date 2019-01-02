package com.company;

import javafx.beans.property.SimpleStringProperty;

public class StudentsModel {

    private SimpleStringProperty subject;
    private SimpleStringProperty topic;
    private SimpleStringProperty eventdate;
    private SimpleStringProperty rating;

    public StudentsModel(String subject, String topic, String eventdate, String rating) {
        this.subject = new SimpleStringProperty(subject);
        this.topic = new SimpleStringProperty(topic);
        this.eventdate = new SimpleStringProperty(eventdate);
        this.rating = new SimpleStringProperty(rating);
    }

    public String getSubject() { return subject.get(); }

    public void setSubject(String subject) {
        this.subject = new SimpleStringProperty(subject);
    }

    public String getTopic() {
        return topic.get();
    }

    public void setTopic(String topic) {
        this.topic = new SimpleStringProperty(topic);
    }

    public String getEventdate() {
        return eventdate.get();
    }

    public void setEventdate(String eventdate) {
        this.eventdate = new SimpleStringProperty(eventdate);
    }

    public String getRating() {
        return rating.get();
    }

    public void setRating(String rating) {
        this.rating = new SimpleStringProperty(rating);
    }
}