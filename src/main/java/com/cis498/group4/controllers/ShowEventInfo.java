package com.cis498.group4.controllers;

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

/**
 * The ShowEventInfo servlet responds with the information for the specified event.
 */
@WebServlet(name = "ShowEventInfo", urlPatterns = "/manager/show-event-info")
public class ShowEventInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EventDataAccess eventData;

    public ShowEventInfo() {
        super();
        eventData = new EventDataAccess();
    }

    /**
     * Render page to display all data about an event, with buttons to edit and delete
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

        String url = "/WEB-INF/views/show-event-info.jsp";

        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("event", event);

        String pageTitle = String.format("Info for %s", event.getName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
