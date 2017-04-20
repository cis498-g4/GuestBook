package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
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
 * Created by mpm624 on 4/18/17.
 */
@WebServlet(name = "ListUserRegsForEvent", urlPatterns = "/manager/view-reg")
public class ListUserRegsForEvent extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDataAccess attendanceData;
    private EventDataAccess eventData;

    public ListUserRegsForEvent(){
        super();
        attendanceData = new AttendanceDataAccess();
        eventData = new EventDataAccess();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Restrict access by non-Organizers
        if (!SessionHelpers.checkOrganizer(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        String url = "/WEB-INF/views/user-reg-list.jsp";

        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("event", event);

        List<Attendance> attendanceList = attendanceData.getEventAttendance(event);
        request.setAttribute("attendanceList", attendanceList);

        // Calculate # of spots remainin
        if (event.getCapacity() > 0) {
            int remain;

            if (!attendanceList.isEmpty()) {
                remain = event.getCapacity() - attendanceList.get(0).getEvent().getNumRegistered();
            } else {
                remain = event.getCapacity();
            }

            request.setAttribute("remain", remain);
        }

        String pageTitle = String.format("Users registered for %s (%s)",
                event.getName(), event.getStartDateTime().format(DateTimeFormatter.ofPattern("M/d/YY")));
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
