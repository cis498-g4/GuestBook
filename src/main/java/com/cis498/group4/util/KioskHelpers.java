package com.cis498.group4.util;

import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;

import java.time.LocalDateTime;

/**
 * The KioskHelpers class contains methods to aid kiosk mode functionality (e.g. validating a sign-in)
 */
public class KioskHelpers {

    public static final int SUCCESS_COMPLETE = 0;
    public static final int SUCCESS_NEED_SURVEY = 1;
    public static final int ALREADY_SIGNED_IN = 2;
    public static final int USER_NOT_FOUND = 3;
    public static final int NEED_REGISTRATION = 4;
    public static final int EVENT_FULL = 5;
    public static final int CLOSED_REGISTRATION = 6;
    public static final int EVENT_ENDED = 7;

    /**
     * Checks sign-in status conditions and returns a status code
     * @param event
     * @param user
     * @param attendance
     * @return
     */
    public static int signInStatus(Event event, User user, Attendance attendance) {

        if (EventHelpers.endedInPast(event)) {
            return EVENT_ENDED;
        }

        if (user.getEmail() == null) {
            return USER_NOT_FOUND;
        }

        if (attendance.getStatus() == null) {
            if (!event.isOpenRegistration()) {
                return CLOSED_REGISTRATION;
            }

            if (EventHelpers.isFull(event)) {
                return EVENT_FULL;
            }

            return NEED_REGISTRATION;
        }

        if (attendance.getStatus() != Attendance.AttendanceStatus.NOT_ATTENDED) {
            return ALREADY_SIGNED_IN;
        }

        if (event.isMandatorySurvey()) {
            return SUCCESS_NEED_SURVEY;
        }

        return SUCCESS_COMPLETE;
    }

}
