package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.models.Event;
import com.cis498.group4.util.SessionHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The ListEventRegistrations servlet lists upcoming events for registration.
 */
@WebServlet(name = "ListEventRegistrations", urlPatterns = "/manager/event-reg-list")
public class ListEventRegistrations extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EventDataAccess eventData;
    private AttendanceDataAccess attendanceData;

    public ListEventRegistrations() {
        super();
        eventData = new EventDataAccess();
        attendanceData = new AttendanceDataAccess();
    }

    /**
     * Render a list of events occurring in the future, with buttons to view users registered for that event, or to
     * register additional users for that event
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access by non-Organizers
        if (!SessionHelpers.checkLogin(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        String url = "/WEB-INF/views/event-reg-list.jsp";
        String pageTitle = "Upcoming Events";

        List<Event> futureEvents = eventData.getFutureEvents();
        Map<Integer, Integer> attendanceCounts = attendanceData.getAttendanceCounts();

        for (Event event: futureEvents) {
            try {
                event.setNumRegistered(attendanceCounts.get(event.getId()));
            } catch (NullPointerException e) {
                event.setNumRegistered(0);
            }
        }

        request.setAttribute("events", futureEvents);

        request.setAttribute("pageTitle", pageTitle);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}