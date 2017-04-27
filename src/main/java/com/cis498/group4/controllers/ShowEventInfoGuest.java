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
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access by non-Guests
        if (!SessionHelpers.checkGuest(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sessionUser");

        String url = "/WEB-INF/views/show-event-guest.jsp";

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

        String pageTitle = String.format("Info for event - %s", event.getName());
        request.setAttribute("pageTitle", pageTitle);

        // Get temporal status of attendance (especially for NOT_ATTENDED)
        String temporalStatus = AttendanceHelpers.temporalStatus(attendance);
        request.setAttribute("temporalStatus", temporalStatus);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
