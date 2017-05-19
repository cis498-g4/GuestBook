package com.cis498.group4.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * The Survey class is a JavaBean data object representing a response to a survey
 */
public class Survey implements Serializable {
    public static final long serialVersionUID = 1L;

    private int id;
    private User user;
    private Event event;
    private LocalDateTime submissionDateTime;
    private Map<String, Integer> responses;

    public Survey() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getSubmissionDateTime() {
        return submissionDateTime;
    }

    public void setSubmissionDateTime(LocalDateTime submissionDateTime) {
        this.submissionDateTime = submissionDateTime;
    }

    public Map<String, Integer> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, Integer> responses) {
        this.responses = responses;
    }

}
