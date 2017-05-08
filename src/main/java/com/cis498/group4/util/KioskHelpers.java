package com.cis498.group4.util;

import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;

/**
 * The KioskHelpers class contains methods to aid kiosk mode functionality (e.g. validating a sign-in)
 */
public class KioskHelpers {

    // Sign-in status codes
    public static final int SUCCESS_COMPLETE = 0;
    public static final int SUCCESS_NEED_SURVEY = 1;
    public static final int SUCCESS_OPEN_REGISTRATION = 2;
    public static final int SUCCESS_OPEN_REG_NEED_SURVEY = 3;
    public static final int ACTION_USER_NOT_FOUND = 4;
    public static final int FAIL_ALREADY_SIGNED_IN = 5;
    public static final int FAIL_EVENT_FULL = 6;
    public static final int FAIL_CLOSED_REGISTRATION = 7;
    public static final int FAIL_EVENT_ENDED = 8;
    public static final int FAIL_INVALID_TYPE = 9;

    /**
     * Checks sign-in status conditions and returns a status code
     * @param event
     * @param user
     * @param attendance
     * @return
     */
    public static int signInStatus(Event event, User user, Attendance attendance) {

        if (EventHelpers.endedInPast(event)) {
            return FAIL_EVENT_ENDED;
        }

        if (user.getEmail() == null) {
            return ACTION_USER_NOT_FOUND;
        }

        if (user.getType() != User.UserType.GUEST) {
            return FAIL_INVALID_TYPE;
        }

        if (attendance.getStatus() != Attendance.AttendanceStatus.NOT_ATTENDED) {
            if (attendance.getStatus() == Attendance.AttendanceStatus.ATTENDED ||
                    attendance.getStatus() == Attendance.AttendanceStatus.SIGNED_IN) {
                return FAIL_ALREADY_SIGNED_IN;
            }

            if (!event.isOpenRegistration()) {
                return FAIL_CLOSED_REGISTRATION;
            }

            if (AttendanceHelpers.isFull(event)) {
                return FAIL_EVENT_FULL;
            }

            // User is not pre-registered, but registration is open
            if (event.isMandatorySurvey()) {
                return SUCCESS_OPEN_REG_NEED_SURVEY;
            }

            return SUCCESS_OPEN_REGISTRATION;
        }

        if (event.isMandatorySurvey()) {
            return SUCCESS_NEED_SURVEY;
        }

        return SUCCESS_COMPLETE;
    }

}
