package com.cis498.group4.util;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;

import java.util.List;

/**
 * The AttendanceHelpers class contains methods to aid with manipulating Attendance data
 */
public class AttendanceHelpers {

    // Registration status codes
    public static final int SUCCESS = 0;
    public static final int ACTION_CLOSED_REGISTRATION = 1;
    public static final int ACTION_NEW_USER = 2;
    public static final int FAIL_INVALID_USER = 3;
    public static final int FAIL_INVALID_USER_TYPE = 4;
    public static final int FAIL_INVALID_EVENT = 5;
    public static final int FAIL_EVENT_FULL = 6;
    public static final int FAIL_EVENT_ENDED = 7;
    public static final int FAIL_REG_OVERLAP = 8;

    /**
     * Validates an attendance record (e.g. no event registrations overlap for that user, capacity not full).
     * Use before writing to database.
     * @param attendance
     * @return
     */
    public static boolean validate(Attendance attendance) {
        //TODO
        return true;
    }

    /**
     * Validates an attendance record (e.g. no event registrations overlap for that user).
     * Use before writing to database.
     * @param user
     * @param event
     * @return
     */
    public static boolean validate(User user, Event event) {
        //TODO no registrations overlap
        //TODO event is not full
        return true;
    }

    /**
     * Validates an attendance status update (e.g. cannot downgrade an attendance from ATTENDED to SIGNED_IN)
     * Use before writing to database.
     * @param attendance
     * @param status
     * @return
     */
    public static boolean validateStatus(Attendance attendance, int status) {
        //TODO
        return true;
    }

    /**
     * Calculates the number of spots remaining at an event by subtracting the event capacity from the number registered
     * @param attendance Attendance record, which returns the event with a numRegistered parameter already set
     * @return Number of spots remaining, or -1 if event has no max capacity
     */
    public static int calculateSpotsRemaining(Attendance attendance) {
        Event event = attendance.getEvent();

        if (event.getCapacity() <= 0) {
            return -1;
        }

        return event.getCapacity() - event.getNumRegistered();

    }

    /**
     * Calculates the number of spots remaining at an event by subtracting the event capacity from the number registered
     * @param event Event, which may or may not have a numRegistered parameter set
     * @return Number of spots remaining, or -1 if event has no max capacity
     */
    public static int calculateSpotsRemaining(Event event) {
        if (event.getCapacity() <= 0) {
            return -1;
        }

        AttendanceDataAccess attendanceData = new AttendanceDataAccess();
        List<Attendance> attendanceList = attendanceData.getEventAttendance(event);
        return event.getCapacity() - attendanceList.size();
    }

    /**
     * Checks whether the registration overlaps with an existing registration
     * @param eventA
     * @return
     */
    public static boolean isOverlapping(Event eventA, List<Attendance> registrations) {
        if (registrations.isEmpty()) {
            return false;
        }

        for (Attendance registration : registrations) {
            Event eventB = registration.getEvent();

            if (eventA.getStartDateTime().isAfter(eventB.getStartDateTime()) &&
                    eventA.getStartDateTime().isBefore(eventB.getEndDateTime())) {
                return true;
            }

            if (eventA.getEndDateTime().isAfter(eventB.getStartDateTime()) &&
                    eventA.getEndDateTime().isBefore(eventB.getEndDateTime())) {
                return true;
            }

        }

        return false;
    }

    /**
     * Checks whether an event is at capacity
     * @param event Event, which may or may not have a numRegistered parameter set
     * @return
     */
    public static boolean isFull(Event event) {
        if (event.getCapacity() <= 0) {
            return false;
        }

        AttendanceDataAccess attendanceData = new AttendanceDataAccess();
        int attendanceCount = attendanceData.getAttendanceCount(event.getId());

        return (attendanceCount >= event.getCapacity());
    }

    /**
     *
     * @param user
     * @param event
     * @return
     */
    public static int registerStatus(User user, Event event, List<Attendance> registrations) {

        if (event.getId() < 1) {
            return FAIL_INVALID_EVENT;
        }

        if (!UserHelpers.validateFields(user)) {
            return FAIL_INVALID_USER;
        }

        if (user.getType() != User.UserType.GUEST) {
            return FAIL_INVALID_USER_TYPE;
        }

        if (EventHelpers.endedInPast(event)) {
            return FAIL_EVENT_ENDED;
        }

        if (isFull(event)) {
            return FAIL_EVENT_FULL;
        }

        if (isOverlapping(event, registrations)) {
            return FAIL_REG_OVERLAP;
        }

        if (user.getId() < 1) {
            return ACTION_NEW_USER;
        }

        if (!event.isOpenRegistration()) {
            return ACTION_CLOSED_REGISTRATION;
        }

        return SUCCESS;
    }

    /**
     * Returns more specific attendance status string, based on whether the event is in the past or future
     * @param attendance
     * @return
     */
    public static String temporalStatus(Attendance attendance) {
        if(attendance.getStatus() == Attendance.AttendanceStatus.NOT_ATTENDED) {
            if (EventHelpers.startsInFuture(attendance.getEvent())) {
                return "upcoming";
            } else if (EventHelpers.isInProgress(attendance.getEvent())) {
                return "current";
            } else {
                return "missed";
            }
        }

        if (attendance.getStatus() == Attendance.AttendanceStatus.SIGNED_IN) {
            if (EventHelpers.endsInFuture(attendance.getEvent())) {
                return "signed-in";
            }

            return "survey-pending";
        }

        if (attendance.getStatus() == Attendance.AttendanceStatus.ATTENDED) {
            if (EventHelpers.endsInFuture(attendance.getEvent())) {
                return "in-attendance";
            }

            return "attended";
        }

        return "unknown";

    }

}
