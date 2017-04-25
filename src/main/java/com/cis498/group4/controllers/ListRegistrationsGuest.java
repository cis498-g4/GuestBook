package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.EventDataAccess;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The ListRegistrationsGuest servlet lists upcoming events for which the user is registered.
 */
@WebServlet(name = "ListRegistrationsGuest", urlPatterns = "/manager/list-registrations-guest")
public class ListRegistrationsGuest extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDataAccess attendanceData;

    public ListRegistrationsGuest() {
        super();
        attendanceData = new AttendanceDataAccess();
    }

    /**
     * Render a list of the user's events occurring in the future, with buttons to deregister for each event,
     * and a form field to register for additional events
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

        String url = "/WEB-INF/views/list-registrations-guest.jsp";
        String pageTitle = "Upcoming Registrations";

        List<Attendance> registrations = attendanceData.getFutureRegistrations(user);
        Map<Integer, Integer> attendanceCounts = attendanceData.getAttendanceCounts();
        request.setAttribute("registrations", registrations);

        request.setAttribute("pageTitle", pageTitle);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Respond to posted form to register for an additional event, if the user knows the code
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO
    }

}
