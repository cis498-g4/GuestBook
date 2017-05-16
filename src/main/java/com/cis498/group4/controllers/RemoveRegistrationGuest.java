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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * The RemoveRegistrationGuest servlet responds to requests to remove a guest registration from an event.
 * This is accomplished by deleting an Event Attendance record from the DB.
 */
@WebServlet(name = "RemoveRegistrationGuest", urlPatterns = "/manager/remove-registration-guest")
public class RemoveRegistrationGuest extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDataAccess attendanceData;

    public RemoveRegistrationGuest() {
        super();
        attendanceData = new AttendanceDataAccess();
    }

    /**
     * Render a confirmation message for the registration record to be removed
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access by non-Guests
        if (!SessionHelpers.checkGuest(request.getSession())) {
            request.setAttribute("statusMessage", "You must be logged in as a guest to view the requested page.");
            request.setAttribute("statusType", "warning");
            RequestDispatcher view = request.getRequestDispatcher("/manager/login");
            view.forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sessionUser");

        String url = "/WEB-INF/views/remove-registration-guest.jsp";
        String pageTitle;
        String back;

        // Get registration data for current session user and selected event, respond with generic error if not found
        try {
            Attendance attendance = attendanceData.getAttendance(
                    user.getId(), Integer.parseInt(request.getParameter("eventId")));

            Event event = attendance.getEvent();
            request.setAttribute("event", event);
            String eventDate = event.getStartDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
            request.setAttribute("eventDate", eventDate);
            String eventLongDate = event.getStartDateTime().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
            request.setAttribute("eventLongDate", eventLongDate);

            // Set warning message
            String warningMessage;

            if (!event.isOpenRegistration()) {
                warningMessage = "<strong>You will not be able to re-register for this event " +
                        "without the assistance of an event organizer!</strong>";
            } else {
                if (event.getRegistrationCode() != null) {
                    warningMessage = String.format(
                            "If you wish to re-register, use the registration code <strong>%s</strong>.",
                            event.getRegistrationCode());
                } else {
                    warningMessage = "This event has open registration but no registration code. " +
                            "You may still sign in freely at the event kiosk.";
                }
            }

            request.setAttribute("warningMessage", warningMessage);

            pageTitle = String.format("Remove your registration from %s on %s?", event.getName(), eventDate);
            back = "list-registrations-guest";

        } catch (Exception e) {
            url = "/WEB-INF/views/error-generic.jsp";
            pageTitle = "Invalid Registration Data";
            back = "list-registrations-guest";
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

        // Restrict access by non-Guests
        if (!SessionHelpers.checkGuest(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sessionUser");

        String url = "/manager/list-registrations-guest";
        String statusMessage;
        String statusType;

        // Remove registration if event not in past, and user has not already signed in
        try {
            Attendance attendance = attendanceData.getAttendance(
                    user.getId(), Integer.parseInt(request.getParameter("eventId")));

            if (attendance.getStatus() == Attendance.AttendanceStatus.NOT_ATTENDED) {
                if (EventHelpers.endsInFuture(attendance.getEvent())) {

                    int deregStatus = attendanceData.deleteAttendance(attendance);

                    // Check status code returned by remove registration operation
                    if (deregStatus == 0) {
                        statusMessage = String.format("Your registration from %s has been removed",
                                attendance.getEvent().getName());
                        statusType = "success";
                    } else {
                        statusMessage = "<strong>Error!</strong> Remove registration operation failed!";
                        statusType = "danger";
                    }

                } else {
                    statusMessage = "<strong>Error!</strong> You cannot remove your registration from an event that has already taken place!";
                    statusType = "danger";
                }
            } else {
                statusMessage = "<strong>Error!</strong> You cannot remove your registration once you have signed in!";
                statusType = "danger";
            }
        } catch (Exception e) {
            statusMessage = "<strong>Error!</strong> Remove registration operation failed!";
            statusType = "danger";
        }

        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
