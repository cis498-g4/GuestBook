package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
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
 * The ShowEventInfoGuest servlet responds with the registration information for the specified event for the current user.
 */
@WebServlet(name = "ShowEventInfoGuest", urlPatterns = "/manager/show-event-guest")
public class ShowEventInfoGuest extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDataAccess attendanceData;

    public ShowEventInfoGuest() {
        super();
        attendanceData = new AttendanceDataAccess();
    }

    /**
     * Render page to display all data about an event associated with a user,
     * with buttons to take surveys or remove registration (if applicable)
     * @param request The HTTP request received from the client
     * @param response The HTTP response returned by the servlet
     * @throws ServletException The request could not be handled
     * @throws IOException An input or output error has occurred
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

        String url = "/WEB-INF/views/show-event-guest.jsp";
        String pageTitle;
        String back = "list-events-guest";

        // Get registration data for current guest, if requested event not found respond with generic error
        try {
            Attendance attendance = attendanceData.getAttendance(user.getId(),
                    Integer.parseInt(request.getParameter("eventId")));

            Event event = attendance.getEvent();
            request.setAttribute("event", event);
            request.setAttribute("eventStart",
                    event.getStartDateTime().format(DateTimeFormatter.ofPattern("M/d/yyyy HH:mm")));
            request.setAttribute("eventEnd",
                    event.getEndDateTime().format(DateTimeFormatter.ofPattern("M/d/yyyy HH:mm")));
            request.setAttribute("eventLongDate",
                    event.getStartDateTime().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));

            // Get temporal status of attendance (especially for NOT_ATTENDED)
            String temporalStatus = AttendanceHelpers.temporalStatus(attendance);
            request.setAttribute("temporalStatus", temporalStatus);

            pageTitle = String.format("Info for event - %s", event.getName());

        } catch (Exception e) {
            pageTitle = "Event Not Found";
            url = "/WEB-INF/views/error-generic.jsp";
            String message = "The event you were attempting to view could not be found.";
            request.setAttribute("message", message);
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
