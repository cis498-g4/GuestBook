package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
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
 * The EventRegistration servlet responds to requests to register users for an event.
 */
@WebServlet(name = "EventRegistration", urlPatterns = "/manager/event-reg")
public class EventRegistration extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDataAccess attendanceData;
    private EventDataAccess eventData;
    private UserDataAccess userData;

    public EventRegistration() {
        super();
        attendanceData = new AttendanceDataAccess();
        eventData = new EventDataAccess();
        userData = new UserDataAccess();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access if not logged in
        if (!SessionHelpers.checkLogin(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You must login to access this resource");
            return;
        }

        String url = "/WEB-INF/views/event-reg.jsp";

        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));
        String eventDate = event.getStartDateTime().format(DateTimeFormatter.ofPattern("MM/dd/YYYY"));
        String pageTitle = String.format("Registration for %s (%s)", event.getName(), eventDate);

        request.setAttribute("event", event);
        request.setAttribute("eventDate", eventDate);
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access if not logged in
        if (!SessionHelpers.checkLogin(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You must login to access this resource");
            return;
        }

        String statusMessage;

        User user = userData.getUserByEmail(request.getParameter("email"));
        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("eventId")));
        request.setAttribute("event", event);

        String url = String.format("/manager/view-reg?id=%d", event.getId());

        //TODO Validate before insert - check capacity
        if (user.getEmail() != null) {
            int insertStatus = attendanceData.register(user, event);

            if (insertStatus == 0) {
                statusMessage = "Registration was successful!";
            } else {
                statusMessage = "ERROR: Registration failed!";
            }

        }  else {
            statusMessage = "ERROR: User not found!!";
        }

        request.setAttribute("statusMessage", statusMessage);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
