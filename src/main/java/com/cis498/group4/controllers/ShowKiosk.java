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
        int statusCode;

        HttpSession session = request.getSession();

        Event event = (Event) session.getAttribute("event");
        User user = userData.getUserByEmail(request.getParameter("email"));
        Attendance attendance = attendanceData.getAttendance(user.getId(), event.getId());

        statusCode = KioskHelpers.signInStatus(event, user, attendance);

        // TODO: Case: Event Status OK, Email OK, Registration Status OK, Survey NOT REQUIRED
        url = "WEB-INF/views/kiosk-success.jsp";


        // TODO: Case: Event Status OK, Email OK, Registration Status OK, Event Status OK, Survey REQUIRED
        url = "WEB-INF/views/kiosk-success.jsp";


        // TODO: Case: Event NOT FULL, Email OK, Registration Status NULL, OPEN registration

        // TODO: Case: Event FULL, Email OK, Registration Status NULL, OPEN registration

        // TODO: Case: Event Status OK, Email OK, Registration Status NULL, CLOSED registration

        // TODO: Case: Event Status OK, Email OK, Registration Status SIGNED_IN or ATTENDED

        // TODO: Case: Event Status OK, Email NULL

        // TODO: Case: Event end time in PAST



    }
}
