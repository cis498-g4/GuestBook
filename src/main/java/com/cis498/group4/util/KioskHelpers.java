package com.cis498.group4.util;

import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;

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
     * Checks sign in status conditions and returns a status code
     * @param event
     * @param user
     * @param attendance
     * @return
     */
    public static int signInStatus(Event event, User user, Attendance attendance) {
        return SUCCESS_COMPLETE;
    }

}
