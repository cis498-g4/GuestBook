package com.cis498.group4.util;

import com.cis498.group4.models.User;

import javax.servlet.http.HttpSession;

/**
 * The SessionHelpers class contains methods to help deal with session data
 */
public class SessionHelpers {

    /**
     * Retrieves the user info stored in the current session
     * @param session The current session
     * @return The user stored in the session
     */
    public static User getSessionUser(HttpSession session) {
        return (User) session.getAttribute("sessionUser");
    }

    /**
     * Stores the specified user in the session
     * @param session The current session
     * @param user The user to store
     */
    public static void setSessionUser(HttpSession session, User user) {
        session.setAttribute("sessionUser", user);
    }

}
