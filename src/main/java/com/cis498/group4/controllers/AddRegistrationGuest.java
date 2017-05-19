package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.AttendanceHelpers;
import com.cis498.group4.util.SessionHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * The AddRegistration servlet responds to requests to register users for an event.
 */
@WebServlet(name = "AddRegistrationGuest", urlPatterns = "/manager/add-registration-guest")
public class AddRegistrationGuest extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDataAccess attendanceData;
    private EventDataAccess eventData;

    public AddRegistrationGuest() {
        super();
        attendanceData = new AttendanceDataAccess();
        eventData = new EventDataAccess();
    }

    /**
     * Respond with method not allowed message
     * @param request The HTTP request received from the client
     * @param response The HTTP response returned by the servlet
     * @throws ServletException The request could not be handled
     * @throws IOException An input or output error has occurred
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // There is no GET action, registration form is rendered on guest registration list
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

    }

    /**
     * Create a new registration (attendance) record from posted data, write to database, and send confirmation to user
     * @param request The HTTP request received from the client
     * @param response The HTTP response returned by the servlet
     * @throws ServletException The request could not be handled
     * @throws IOException An input or output error has occurred
     */
    @Override
    @SuppressWarnings("unchecked")
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
        String pageTitle = "Upcoming Registrations";
        String statusMessage;
        String statusType;

        String registrationCode = request.getParameter("reg-code");
        Event event = eventData.getEventByRegistrationCode(registrationCode);

        // Get registrations from session, or if unavailable, re-query DB
        List<Attendance> registrations;

        if (session.getAttribute("registrations") instanceof List<?>) {
            registrations = (List<Attendance>) session.getAttribute("registrations");
        } else {
            registrations = attendanceData.getFutureRegistrations(user);
        }

        // Perform appropriate registration action / respond with appropriate message
        int status = AttendanceHelpers.registerStatus(user, event, registrations);

        switch(status) {
            case AttendanceHelpers.SUCCESSFUL_REGISTRATION:
                int insertStatus = attendanceData.insertAttendance(user, event);
                if (insertStatus == 0) {
                    statusMessage = String.format("Successfully registered for %s!", event.getName());
                    statusType = "success";
                } else if (insertStatus == 1062) {
                    statusMessage = String.format("You are already registered for %s!", event.getName());
                    statusType = "warning";
                } else {
                    statusMessage = String.format(
                            "<strong>Error!</strong> There was a problem processing your registration %d", insertStatus);
                    statusType = "danger";
                }
                break;
            case AttendanceHelpers.CLOSED_REGISTRATION:
                statusMessage = String.format("<strong>Error!</strong> %s does not have open registration. " +
                        "Please contact an event organizer to register.", event.getName());
                statusType = "danger";
                break;
            case AttendanceHelpers.EVENT_ENDED:
                statusMessage = String.format("<strong>Error!</strong> %s has already concluded.", event.getName());
                statusType = "danger";
                break;
            case AttendanceHelpers.EVENT_FULL:
                statusMessage = String.format("<strong>Error!</strong> The event %s is at capacity.", event.getName());
                statusType = "danger";
                break;
            case AttendanceHelpers.INVALID_EVENT:
                statusMessage = String.format(
                        "<strong>Error!</strong> No event was found with the registration code %s", registrationCode);
                statusType = "danger";
                break;
            case AttendanceHelpers.REGISTRATION_OVERLAP:
                statusMessage = String.format(
                        "<strong>Error!</strong> %s overlaps one of your existing registrations.", event.getName());
                statusType = "danger";
                break;
            default:
                statusMessage = "<strong>Error!</strong> There was a problem processing your registration.";
                statusType = "danger";
                break;
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
