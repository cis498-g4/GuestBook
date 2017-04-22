package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.EventHelpers;
import com.cis498.group4.util.KioskHelpers;
import com.cis498.group4.util.SessionHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;

/**
 * The ShowKiosk servlet responds to requests to show the sign-in kiosk for an event
 */
@WebServlet(name = "ShowKiosk", urlPatterns = "/kiosk")
public class ShowKiosk extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDataAccess attendanceData;
    private UserDataAccess userData;

    public ShowKiosk() {
        super();
        attendanceData = new AttendanceDataAccess();
        userData = new UserDataAccess();
    }

    /**
     * Verify that an event has been set, and no user is logged in to the console, then render the event sign-in form
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        String url = "/WEB-INF/views/kiosk-signin.jsp";

        // If there is a logged in organizer, redirect to event selection, otherwise to error message
        if (session.getAttribute("sessionUser") != null) {
            User sessionUser = (User) session.getAttribute("sessionUser");

            if (SessionHelpers.checkOrganizer(request.getSession())) {
                response.sendRedirect("/manager/start-kiosk");
                return;
            }

            response.sendRedirect("/WEB-INF/views/bad-session.jsp");
            return;
        }

        // If no session event, redirect to error message
        if (session.getAttribute("event") == null) {
            response.sendRedirect("/WEB-INF/views/bad-session.jsp");
            return;
        }

        // Render form for sign-in
        Event event = (Event) session.getAttribute("event");
        request.setAttribute("event", event);

        // Set session expiration time to number of seconds between event end time and now
        session.setMaxInactiveInterval(EventHelpers.secondsToEnd(event));

        String pageTitle = String.format("Welcome to %s", event.getName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url;
        int status;

        HttpSession session = request.getSession();

        Event event = (Event) session.getAttribute("event");
        User user = userData.getUserByEmail(request.getParameter("email"));
        Attendance attendance = attendanceData.getAttendance(user.getId(), event.getId());

        status = KioskHelpers.signInStatus(event, user, attendance);

        switch (status) {
            // Case 0: Success: User exists and is pre-registered, Survey NOT required
            case KioskHelpers.SUCCESS_COMPLETE:
                attendanceData.updateStatus(attendance, Attendance.AttendanceStatus.ATTENDED.ordinal());
                url = "WEB-INF/views/kiosk-success.jsp";
                break;
            // Case 1: Success: User exists and is pre-registered, Survey required
            case KioskHelpers.SUCCESS_NEED_SURVEY:
                attendanceData.updateStatus(attendance, Attendance.AttendanceStatus.SIGNED_IN.ordinal());
                url = "WEB-INF/views/kiosk-success.jsp";
                break;
            // Case 2: Success: User exists and is not pre-registered, but registration is open, Survey NOT required
            case KioskHelpers.SUCCESS_OPEN_REGISTRATION:
                attendanceData.register(user, event, Attendance.AttendanceStatus.ATTENDED.ordinal());  // TODO: How to error check? should this be in the data access methods?
                url = "WEB-INF/views/kiosk-success.jsp";
                break;
            // Case 3: Success: User exists and is not pre-registered, but registration is open, Survey required
            case KioskHelpers.SUCCESS_OPEN_REG_NEED_SURVEY:
                attendanceData.register(user, event, Attendance.AttendanceStatus.SIGNED_IN.ordinal());
                url = "WEB-INF/views/kiosk-success.jsp";
                break;
            // Case 4: Action needed: User email not found. Try again or create new
            case KioskHelpers.ACTION_USER_NOT_FOUND:
                url = "/WEB-INF/views/kiosk-user-not-found.jsp";
                request.setAttribute("email", request.getParameter("email"));
                break;
            // Case 5: Failure: User already signed in
            case KioskHelpers.FAIL_ALREADY_SIGNED_IN:
                url = "/WEB-INF/views/kiosk-fail.jsp";
                break;
            // Case 6: Failure: User is not registered and event is full. No new registrations allowed.
            case KioskHelpers.FAIL_EVENT_FULL:
                url = "/WEB-INF/views/kiosk-fail.jsp";
                break;
            // Case 7: Failure: User is not registered and the event does not have open registration.
            case KioskHelpers.FAIL_CLOSED_REGISTRATION:
                url = "/WEB-INF/views/kiosk-fail.jsp";
                break;
            // Case 8: Failure: Event has ended. Users may no longer sign in
            case KioskHelpers.FAIL_EVENT_ENDED:
                url = "/WEB-INF/views/kiosk-fail.jsp";
                break;
            // Default: Fail closed with generic error message
            default:
                url = "WEB-INF/views/kiosk-fail.jsp";
                break;
        }

        if (status != KioskHelpers.ACTION_USER_NOT_FOUND) {
            request.setAttribute("user", user);
        }

        request.setAttribute("status", status);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
