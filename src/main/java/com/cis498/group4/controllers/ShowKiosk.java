package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.AttendanceHelpers;
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

/**
 * The ShowKiosk servlet responds to requests to show the sign-in kiosk for an event
 */
@WebServlet(name = "ShowKiosk", urlPatterns = {"/kiosk", "/home", "/kiosk/home"})
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

        String url;
        String pageTitle;

        // If there is a logged in organizer, redirect to event selection, otherwise to error message
        if (session.getAttribute("sessionUser") != null) {
            User sessionUser = (User) session.getAttribute("sessionUser");

            if (SessionHelpers.checkOrganizer(request.getSession())) {
                response.sendRedirect(String.format("%s/manager/start-kiosk", request.getContextPath()));
                return;
            }

            response.sendRedirect("index.jsp");
            return;
        }

        // If no session event, redirect to error message
        if (session.getAttribute("event") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // TODO Render form for sign-in, or alert page if event is full or ended
        Event event = (Event) session.getAttribute("event");
        request.setAttribute("event", event);

        String message;
        if (EventHelpers.endedInPast(event)) {
            url = "/WEB-INF/views/kiosk-lock.jsp";
            pageTitle = String.format("%s has concluded", event.getName());
            message = "This event has concluded and is no longer accepting sign-ins.";
            request.setAttribute("message", message);
        } else if (AttendanceHelpers.isFullSignIn(event)) {
            url = "/WEB-INF/views/kiosk-lock.jsp";
            pageTitle = String.format("%s is full", event.getName());
            message = "This event is at capacity and is no longer accepting sign-ins.";
            request.setAttribute("message", message);
        } else {
            // Event OK, render form
            url = "/WEB-INF/views/kiosk-signin.jsp";
            pageTitle = String.format("Welcome to %s", event.getName());

            // Set session expiration time to number of seconds between event end time and now
            session.setMaxInactiveInterval(EventHelpers.secondsToEnd(event));
        }

        request.setAttribute("pageTitle", pageTitle);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "WEB-INF/views/kiosk-message.jsp";
        int status;
        String pageTitle;
        String message1;
        String message2;

        HttpSession session = request.getSession();

        String email = request.getParameter("email");

        Event event = (Event) session.getAttribute("event");
        User user = userData.getUserByEmail(request.getParameter("email"));
        Attendance attendance = attendanceData.getAttendance(user.getId(), event.getId());

        // Perform appropriate sign in action / respond with appropriate sign in message
        status = KioskHelpers.signInStatus(event, user, attendance);

        switch (status) {
            // Case 0: Success: User exists and is pre-registered, Survey NOT required
            case KioskHelpers.SUCCESS_COMPLETE:
                attendanceData.updateStatus(attendance, Attendance.AttendanceStatus.ATTENDED.ordinal()); // TODO: How to error check? should this be in the data access methods?
                pageTitle = "Sign-in Success!";
                message1 = String.format("You have been signed in to %s.", event.getName());
                message2 = "No further action is required on your part. Enjoy the event!";
                break;
            // Case 1: Success: User exists and is pre-registered, Survey required
            case KioskHelpers.SUCCESS_NEED_SURVEY:
                attendanceData.updateStatus(attendance, Attendance.AttendanceStatus.SIGNED_IN.ordinal());
                pageTitle = "Sign-in Success!";
                message1 = String.format("You have been signed in to %s.", event.getName());
                message2 = "<strong>PLEASE NOTE:</strong> " +
                           "Your attendance status will not be counted until you complete the post-event survey!";
                break;
            // Case 2: Success: User exists and is not pre-registered, but registration is open, Survey NOT required
            case KioskHelpers.SUCCESS_OPEN_REGISTRATION:
                attendanceData.insertAttendance(user, event, Attendance.AttendanceStatus.ATTENDED.ordinal());
                pageTitle = "Sign-in Success!";
                message1 = String.format("You have been registered and signed in to %s.", event.getName());
                message2 = "No further action is required on your part. Enjoy the event!";
                break;
            // Case 3: Success: User exists and is not pre-registered, but registration is open, Survey required
            case KioskHelpers.SUCCESS_OPEN_REG_NEED_SURVEY:
                attendanceData.insertAttendance(user, event, Attendance.AttendanceStatus.SIGNED_IN.ordinal());
                pageTitle = "Sign-in Success!";
                message1 = String.format("You have been registered and signed in to %s.", event.getName());
                message2 = "<strong>PLEASE NOTE:</strong> " +
                           "Your attendance status will not be counted until you complete the post-event survey!";
                break;
            // Case 4: Action needed: User email not found. Try again or create new
            case KioskHelpers.ACTION_USER_NOT_FOUND:
                request.setAttribute("email", email);
                pageTitle = "User Not Found";
                message1 = String.format("No user with the email address %s was found.", email);
                message2 = "You may try again, or create a new user.";
                break;
            // Case 5: Failure: User already signed in
            case KioskHelpers.FAIL_ALREADY_SIGNED_IN:
                pageTitle = "Already Signed In";
                message1 = String.format("The user with email address, %s, has already signed in to this event!", email);
                message2 = "No further action is required on your part.";
                break;
            // Case 6: Failure: User is not registered and event is full. No new registrations allowed.
            case KioskHelpers.FAIL_EVENT_FULL:
                pageTitle = "Sign-in Failure: Event is Full";
                message1 = String.format("The registration for %s is at capacity. No new registrations are allowed",
                                         event.getName());
                message2 = "We apologize for any inconvenience. Thank you for your interest in the event.";
                break;
            // Case 7: Failure: User is not registered and the event does not have open registration.
            case KioskHelpers.FAIL_CLOSED_REGISTRATION:
                pageTitle = "Sign-in Failure: Not Registered";
                message1 = String.format("You must be pre-registered to sign-in for %s", event.getName());
                message2 = "Please contact an event organizer for assistance.";
                break;
            // Case 8: Failure: Event has ended. Users may no longer sign in
            case KioskHelpers.FAIL_EVENT_ENDED:
                pageTitle = "Sign-in Failure: Event Ended";
                message1 = String.format("The event %s occurs in the past.", event.getName());
                message2 = "Thank you for your interest. We hope to see you in the future.";
                break;
            // Case 9: Failure: Non-Guest type user attempts to sign in
            case KioskHelpers.FAIL_INVALID_TYPE:
                pageTitle = "Sign-in Failure: Invalid guest";
                message1 = "Only users of type \"Guest\" should sign in.";
                message2 = String.format("Please change the user type or set up a new Guest account for %s %s.",
                        user.getFirstName(), user.getLastName());
                break;
            // Default: Fail closed with generic error message
            default:
                pageTitle = "Sign-in Failure";
                message1 = "There was a problem signing in.";
                message2 = "Please try again or contact an event organizer for assistance.";
                break;
        }

        if (status != KioskHelpers.ACTION_USER_NOT_FOUND) {
            request.setAttribute("user", user);
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("status", status);
        request.setAttribute("message1", message1);
        request.setAttribute("message2", message2);

        RequestDispatcher view = request.getRequestDispatcher(url);
        response.setHeader("Refresh", "10;url=" + request.getContextPath() + "/kiosk");
        view.forward(request, response);

    }

}
