package com.cis498.group4.util;

import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.models.Event;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * The EventHelpers class contains methods to assist with Event data (verification, etc)
 */
public class EventHelpers {

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

}
