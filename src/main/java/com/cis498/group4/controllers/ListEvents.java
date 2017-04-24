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
import java.util.List;

/**
 * The ListEvents servlet responds to requests to view a list of events.
 */
@WebServlet(name = "ListEvents", urlPatterns = "/manager/list-events")
public class ListEvents extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EventDataAccess eventData;

    public ListEvents() {
        super();
        eventData = new EventDataAccess();
    }

    /**
     * Render a list of events, with buttons to view, edit, or delete each
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access by non-Organizers
        // TODO: how to handle filters? Re-call the servlet and pass a filter parameter?
        if (!SessionHelpers.checkOrganizer(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        String url = "/WEB-INF/views/list-events.jsp";

        List<Event> events = eventData.getAllEvents();
        request.setAttribute("events", events);

        String pageTitle = "Events";
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
