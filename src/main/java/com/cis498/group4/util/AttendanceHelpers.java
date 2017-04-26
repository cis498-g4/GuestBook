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
     * @param user
     * @param event
     * @return
     */
    public static boolean isOverlapping(User user, Event event) {
        boolean overlap = false;

        // TODO lookup attendance record for user, event. If exists true, otherwise false
        // TODO Is it better to simply have the DBMS enforce this?

        return overlap;
    }

    /**
     * Checks whether an event is at capacity
     * @param attendance Attendance record, which returns the event with a numRegistered parameter already set
     * @return
     */
    public static boolean isFull(Attendance attendance) {
        Event event = attendance.getEvent();

        if (event.getCapacity() <= 0) {
            return false;
        }

        return (event.getNumRegistered() >= event.getCapacity());
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
