package com.cis498.group4.models;

import java.io.Serializable;

/**
 * The Attendance class is a JavaBean data object representing a user's attendance status at an event
 */
public class Attendance implements Serializable {
    public static final long serialVersionUID = 1L;

    public enum AttendanceStatus {NOT_ATTENDED, SIGNED_IN, ATTENDED};

    private Event event;
    private User user;
    private AttendanceStatus status;

    public Attendance() {
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

}
