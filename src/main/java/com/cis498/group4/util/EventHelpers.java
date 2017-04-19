package com.cis498.group4.util;

import com.cis498.group4.models.Event;

import java.time.LocalDateTime;

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
     * Checks whether an event is at capacity
     * @param event
     * @return
     */
    public static boolean isAtCapacity(Event event) {
        boolean atCapacity = false;

        // TODO

        return atCapacity;
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

}
