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
@WebServlet(name = "RemoveRegistration", urlPatterns = "/manager/remove-registration")
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

        // Restrict access by non-Organizers
        if (!SessionHelpers.checkOrganizer(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        String url = "/WEB-INF/views/remove-registration.jsp";
        String pageTitle;
        String back;

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int eventId = Integer.parseInt(request.getParameter("eventId"));

            Attendance attendance = attendanceData.getAttendance(userId, eventId);
            User user = attendance.getUser();
            Event event = attendance.getEvent();
            request.setAttribute("attendance", attendance);
            request.setAttribute("user", user);
            request.setAttribute("event", event);

            String eventDate = event.getStartDateTime().format(DateTimeFormatter.ofPattern("M/d/YY"));
            request.setAttribute("eventDate", eventDate);
            String eventLongDate = event.getStartDateTime().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
            request.setAttribute("eventLongDate", eventLongDate);

            pageTitle = String.format("Remove user %s %s from %s on %s?",
                    user.getFirstName(), user.getLastName(), event.getName(), eventDate);
            back = String.format("list-user-regs-for-event?id=%d", eventId);

        } catch (Exception e) {
            url = "/WEB-INF/views/error-generic.jsp";
            pageTitle = "Invalid Registration Data";
            back = "list-event-registrations";
            String message = "The registration data you were looking for could not be found.";
            request.setAttribute("message", message);
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
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

        // Restrict access by non-Organizers
        if (!SessionHelpers.checkOrganizer(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        String url;
        String statusMessage;
        String statusType;

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int eventId = Integer.parseInt(request.getParameter("eventId"));

            Attendance attendance = attendanceData.getAttendance(userId, eventId);

            url = String.format("list-user-regs-for-event?id=%d", eventId);

            if (attendance.getStatus() == Attendance.AttendanceStatus.NOT_ATTENDED) {
                if (EventHelpers.endsInFuture(attendance.getEvent())) {

                    int deregStatus = attendanceData.deleteAttendance(attendance);

                    // Check status code returned by remove registration operation
                    if (deregStatus == 0) {
                        statusMessage = "Registration has been removed";
                        statusType = "success";
                    } else {
                        statusMessage = "<strong>Error!</strong> Remove registration operation failed!";
                        statusType = "danger";
                    }

                } else {
                    statusMessage = "<strong>Error!</strong> You cannot remove a guest's registration from an event that has already taken place!";
                    statusType = "danger";
                }
            } else {
                statusMessage = "<strong>Error!</strong> You cannot remove a guest's registration if they have already signed in!";
                statusType = "danger";
            }
        } catch (Exception e) {
            statusMessage = "<strong>Error!</strong> Remove registration operation failed!";
            statusType = "danger";
            url = "list-event-registrations";
        }

        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
