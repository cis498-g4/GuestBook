package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.UserDataAccess;
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
import java.time.format.DateTimeFormatter;

/**
 * The AddRegistration servlet responds to requests to register users for an event.
 */
@WebServlet(name = "AddRegistrationGuest", urlPatterns = "/manager/add-registration-guest")
public class AddRegistrationGuest extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDataAccess attendanceData;
    private EventDataAccess eventData;
    private UserDataAccess userData;

    public AddRegistrationGuest() {
        super();
        attendanceData = new AttendanceDataAccess();
        eventData = new EventDataAccess();
        userData = new UserDataAccess();
    }

    /**
     * Respond with method not allowed message
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

    }

    /**
     * Create a new registration (attendance) record from posted data, write to database, and send confirmation to user
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

        String registrationCode = request.getParameter("reg-code");
        Event event = eventData.getEventByRegistrationCode(registrationCode);
        event.setNumRegistered(attendanceData.getAttendanceCount(event.getId()));
        request.setAttribute("event", event);


        // TODO: move these checks into a helper method that returns a status code
        if (event.getName() != null) {
            if (event.isOpenRegistration()) {
                if (!AttendanceHelpers.isFull(event)) {
                    if (!AttendanceHelpers.isOverlapping(user, event)) {

                        int insertStatus = attendanceData.register(user, event);

                        if (insertStatus == 0) {
                            statusMessage = String.format("Successfully registered for %s!", event.getName());
                        } else {
                            statusMessage = String.format(
                                    "ERROR: There was a problem processing your registration %d", insertStatus);
                        }

                    } else {
                        statusMessage = "ERROR: This registration overlaps with another one of your events!";
                    }
                } else {
                    statusMessage = String.format("ERROR: The event %s is at capacity.", event.getName());
                }
            } else {
                statusMessage = String.format("ERROR: %s does not have open registration. " +
                        "Please contact an event organizer to register.", event.getName());
            }
        } else {
            statusMessage = String.format("ERROR: No event was found with the registration code %s", registrationCode);
        }

        request.setAttribute("statusMessage", statusMessage);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
