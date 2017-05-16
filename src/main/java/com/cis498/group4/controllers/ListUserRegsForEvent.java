package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.util.AttendanceHelpers;
import com.cis498.group4.util.SessionHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The ListUserRegsForEvent servlet responds to requests to view all users registered for a specified event
 */
@WebServlet(name = "ListUserRegsForEvent", urlPatterns = "/manager/list-user-regs-for-event")
public class ListUserRegsForEvent extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDataAccess attendanceData;
    private EventDataAccess eventData;

    public ListUserRegsForEvent(){
        super();
        attendanceData = new AttendanceDataAccess();
        eventData = new EventDataAccess();
    }

    /**
     * Render a list of users with registrations for the specified event
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Restrict access by non-Organizers
        if (!SessionHelpers.checkOrganizer(request.getSession())) {
            request.setAttribute("statusMessage", "You must be logged in as an organizer to view the requested page.");
            request.setAttribute("statusType", "warning");
            RequestDispatcher view = request.getRequestDispatcher("/manager/login");
            view.forward(request, response);
            return;
        }

        String url = "/WEB-INF/views/list-user-regs-for-event.jsp";
        String pageTitle;
        String back = "list-event-registrations";

        // Get data, if not found, send generic error
        try {
            Event event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));
            request.setAttribute("event", event);

            List<Attendance> attendanceList = attendanceData.getEventAttendance(event);
            request.setAttribute("attendanceList", attendanceList);

            // Calculate # of spots remaining
            int remain = event.getCapacity();

            if (!attendanceList.isEmpty()) {
                remain = AttendanceHelpers.calculateSpotsRemaining(attendanceList.get(0));
            }

            // Set attributes and render page
            pageTitle = String.format("Users registered for %s %s",
                    event.getName(), event.getStartDateTime().format(DateTimeFormatter.ofPattern("M/d/YY")));

            request.setAttribute("remain", remain);

        } catch (Exception e) {
            pageTitle = "Registration Data Not Found";
            url = "/WEB-INF/views/error-generic.jsp";
            String message = "The registration data you requested could not be found.";
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
