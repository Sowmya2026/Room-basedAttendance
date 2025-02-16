package com.example.roombasedattendance;
public class MessFeedback {
    private String feedbackDescription;
    private String eventDate;
    private String timestamp;

    // Default constructor for Firebase
    public MessFeedback() {}

    // Constructor
    public MessFeedback(String feedbackDescription, String eventDate, String timestamp) {
        this.feedbackDescription = feedbackDescription;
        this.eventDate = eventDate;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getFeedbackDescription() {
        return feedbackDescription;
    }

    public void setFeedbackDescription(String feedbackDescription) {
        this.feedbackDescription = feedbackDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
