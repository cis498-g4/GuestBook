package com.cis498.group4.util;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;

import java.util.List;

/**
 * The AttendanceHelpers class contains methods to aid with manipulating Attendance data
 */
public class AttendanceHelpers {

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
        List<Attendance> attendanceList = attendanceData.getEventAttendance(event);

        return (attendanceList.size() >= event.getCapacity());
    }

}
