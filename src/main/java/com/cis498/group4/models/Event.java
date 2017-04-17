package com.cis498.group4.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The Event class is a JavaBean data object representing an event
 */
public class Event implements Serializable {
    public static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private User presenter;
    private String registrationCode;
    private boolean openRegistration;
    private boolean mandatorySurvey;
    private int capacity;
    private int numRegistered;

    public Event() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public User getPresenter() {
        return presenter;
    }

    public void setPresenter(User presenter) {
        this.presenter = presenter;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public boolean isOpenRegistration() {
        return openRegistration;
    }

    public void setOpenRegistration(boolean openRegistration) {
        this.openRegistration = openRegistration;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isMandatorySurvey() {
        return mandatorySurvey;
    }

    public void setMandatorySurvey(boolean mandatorySurvey) {
        this.mandatorySurvey = mandatorySurvey;
    }

    public int getNumRegistered() {
        return numRegistered;
    }

    public void setNumRegistered(int numRegistered) {
        this.numRegistered = numRegistered;
    }
}
