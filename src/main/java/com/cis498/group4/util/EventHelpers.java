package com.cis498.group4.util;

import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.models.Event;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * The EventHelpers class contains methods to assist with Event data (verification, etc)
 */
public class EventHelpers {

    // Event status codes
    public static final int SUCCESSFUL_WRITE = 0;
    public static final int INVALID_DATA = 1;
    public static final int CONCLUDED = 2;
    public static final int START_IN_PAST = 3;
    public static final int END_BEFORE_START = 4;
    public static final int INVALID_PRESENTER = 5;
    public static final int OVERLAPPING_PRESENTER = 6;
    public static final int INVALID_CAPACITY = 7;
    public static final int INVALID_CODE = 8;

    /**
     * Validates an event record (e.g. no events are scheduled at the same time).
     * Use before writing to database.
     * @param event
     * @return
     */
    public static boolean validate(Event event) {
        //TODO
        return true;
    }

    /**
     * Verifies that the registration code is exactly eight alphanumeric characters
     * @param code
     */
    public static boolean validateRegistrationCode(String code) {

    }

    /**
     * Checks whether the event overlaps with an existing event with the same presenter
     * @param event
     * @return
     */
    public static boolean isOverlapping(Event event) {
        boolean overlap = false;

        // TODO

        return overlap;
    }

    /**
     * Checks whether an event's start time is in the future
     * @param event
     * @return True if the event's start time is after now
     */
    public static boolean startsInFuture(Event event) {

        if (event.getStartDateTime().isAfter(LocalDateTime.now())) {
            return true;
        }

        return false;
    }

    /**
     * Checks whether an event's end time is in the future
     * @param event
     * @return True if the event's end time is after now
     */
    public static boolean endsInFuture(Event event) {

        if (event.getEndDateTime().isAfter(LocalDateTime.now())) {
            return true;
        }

        return false;
    }

    /**
     * Checks whether an event's start time is in the past
     * @param event
     * @return True if the event's start time is before now
     */
    public static boolean startedInPast(Event event) {

        if (event.getStartDateTime().isBefore(LocalDateTime.now())) {
            return true;
        }

        return false;
    }

    /**
     * Checks whether an event's end time is in the past
     * @param event
     * @return True if the event's end time is before now
     */
    public static boolean endedInPast(Event event) {

        if (event.getEndDateTime().isBefore(LocalDateTime.now())) {
            return true;
        }

        return false;
    }

    /**
     * Checks whther the event is currently in progress, based on its scheduled start and end times
     * @param event
     * @return
     */
    public static boolean isInProgress(Event event) {

        if (startedInPast(event) && endsInFuture(event)) {
            return true;
        }

        return false;
    }

    /**
     * Calculates the number of seconds until the event's end
     * @param event
     * @return Number of seconds until event end, or INT_MAX
     */
    public static int secondsToEnd(Event event) {
        long seconds = LocalDateTime.now().until(event.getEndDateTime(), ChronoUnit.SECONDS);

        if (seconds < Integer.MAX_VALUE) {
            return (int) seconds;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Get status code for verifying the success or failure of an insert or update operation
     * @param event
     * @return
     */
    public static int writeStatus(Event event) {

        if (false /*TODO*/) {
            return INVALID_DATA;
        }

        if (startedInPast(event)) {
            return START_IN_PAST;
        }

        if (event.getEndDateTime().isBefore(event.getStartDateTime())) {
            return END_BEFORE_START;
        }

        if (endedInPast(event)) {
            return CONCLUDED;
        }

        if (false /*TODO*/) {
            return INVALID_PRESENTER;
        }

        if (isOverlapping(event)) {
            return OVERLAPPING_PRESENTER;
        }

        if (false /*TODO*/) {
            return INVALID_CAPACITY;
        }

        if (false /*TODO*/) {
            return INVALID_CODE;
        }

        return SUCCESSFUL_WRITE;
    }

}
