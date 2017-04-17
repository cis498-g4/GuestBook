package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Event;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The EventRegistrationList servlet lists upcoming events for registration.
 */
@WebServlet(name = "EventRegistrationList", urlPatterns = "/manager/event-reg-list")
public class EventRegistrationList extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EventDataAccess eventData;
    private UserDataAccess userData;
    private AttendanceDataAccess attendanceData;

    public EventRegistrationList() {
        super();
        eventData = new EventDataAccess();
        userData = new UserDataAccess();
        attendanceData = new AttendanceDataAccess();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/views/event-reg-list.jsp";
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
