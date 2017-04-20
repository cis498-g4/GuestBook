package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.EventHelpers;
import com.cis498.group4.util.SessionHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * The RemoveRegistration servlet responds to requests to remove a user registration from an event.
 * This is accomplished by deleting an Event Attendance record from the DB.
 */
@WebServlet(name = "RemoveRegistration", urlPatterns = "/manager/deregister")
public class RemoveRegistration extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDataAccess attendanceData;

    public RemoveRegistration() {
        super();
        attendanceData = new AttendanceDataAccess();
    }

    /**
     * Render a confirmation message for the registration record to be removed (cancelled)
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access if not logged in
        if (!SessionHelpers.checkLogin(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You must login to access this resource");
            return;
        }

        String url = "/WEB-INF/views/deregister.jsp";

        int userId = Integer.parseInt(request.getParameter("userId"));
        int eventId = Integer.parseInt(request.getParameter("eventId"));

        Attendance attendance = attendanceData.getAttendance(userId, eventId);
        User user = attendance.getUser();
        Event event = attendance.getEvent();
        String eventDate = event.getStartDateTime().format(DateTimeFormatter.ofPattern("MM/dd/YY"));

        request.setAttribute("attendance", attendance);
        request.setAttribute("user", user);
        request.setAttribute("event", event);
        request.setAttribute("eventDate", eventDate);

        String pageTitle = String.format("Remove user %s %s registration from %s (%s)?",
                user.getFirstName(), user.getLastName(), event.getName(), eventDate);
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Receive deletion confirmation, process the deletion in the database, and respond with a confirmation message
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access if not logged in
        if (!SessionHelpers.checkLogin(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You must login to access this resource");
            return;
        }

        int userId = Integer.parseInt(request.getParameter("userId"));
        int eventId = Integer.parseInt(request.getParameter("eventId"));

        Attendance attendance = attendanceData.getAttendance(userId, eventId);

        String url = String.format("/manager/view-reg?id=%d", eventId);
        String statusMessage;

        if (attendance.getStatus() == Attendance.AttendanceStatus.NOT_ATTENDED) {
            if (EventHelpers.endsInFuture(attendance.getEvent())) {

                int deregStatus = attendanceData.deregister(attendance);

                // Check status code returned by deregister operation
                if (deregStatus == 0) {
                    statusMessage = "Registration has been removed";
                } else {
                    statusMessage = "ERROR: Delete event operation failed!";
                }

            } else {
                statusMessage = "ERROR: You cannot de-register a guest from an event that has already taken place!";
            }
        } else {
            statusMessage = "ERROR: You cannot deregister a guest if they have already signed in!";
        }

        request.setAttribute("statusMessage", statusMessage);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
