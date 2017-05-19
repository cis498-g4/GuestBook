package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
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
import java.util.ArrayList;
import java.util.List;

/**
 * The ListPendingSurveysGuest servlet responds to requests to show events where the user has not yet taken a survey.
 */
@WebServlet(name = "ListPendingSurveysGuest", urlPatterns = "/manager/list-surveys-guest")
public class ListPendingSurveysGuest extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AttendanceDataAccess attendanceData;

    public ListPendingSurveysGuest() {
        super();
        attendanceData = new AttendanceDataAccess();
    }

    /**
     * Renders a list of all events for which the current user has attended or signed in, but has not completed a survey
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

        String url = "/WEB-INF/views/list-surveys-guest.jsp";
        String pageTitle = "Pending Surveys";

        // Get attendance data with pending surveys from DB, extract only event information
        List<Attendance> pendingSurveys = attendanceData.getPendingSurveys(user);
        List<Event> pendingSurveyEvents = new ArrayList<Event>();

        for (Attendance attendance : pendingSurveys) {
            pendingSurveyEvents.add(attendance.getEvent());
        }

        request.setAttribute("events", pendingSurveyEvents);

        request.setAttribute("pageTitle", pageTitle);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
