package com.cis498.group4.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Iterator;
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

    // TODO: Default constructor for testing. Remove for production.
    public Survey(int id, User user, Event event, LocalDateTime submissionDateTime, Map<String, Integer> responses) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.submissionDateTime = submissionDateTime;
        this.responses = responses;
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

    /**
     * Calculates and returns the arithmetic mean for this Survey
     * @return BigDecimal average with scale of 2
     */
    public BigDecimal responseAvg() {
        BigDecimal sum = new BigDecimal(0);

        Iterator<Integer> it = getResponses().values().iterator();
        while (it.hasNext()) {
            sum = sum.add(new BigDecimal(it.next()));
        }

        return sum.divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);

    }

}
