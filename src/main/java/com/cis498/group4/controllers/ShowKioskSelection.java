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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * The ShowKioskSelection servlet responds to requests to start the sign in kiosk for an event.
 * Organizer selects the event from a menu, and the kiosk is launched for that event
 */
@WebServlet(name = "ShowKioskSelection", urlPatterns = "/manager/start-kiosk")
public class ShowKioskSelection extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EventDataAccess eventData;

    public ShowKioskSelection() {
        super();
        eventData = new EventDataAccess();
    }

    /**
     * Render form that allows selection of event for kiosk
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access by non-Organizers
        if (!SessionHelpers.checkOrganizer(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        String url = "/WEB-INF/views/select-kiosk.jsp";
        String pageTitle = "Kiosk Event Selection";

        // Provide a list of Events that end in the future for the selection dropdown
        List<Event> futureEvents = eventData.getFutureEvents();
        request.setAttribute("futureEvents", futureEvents);

        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Redirect to sign-in kiosk for the selected event
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access by non-Organizers
        if (!SessionHelpers.checkOrganizer(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        String url = "/kiosk";

        // Invalidate organizer session and store event data in session
        HttpSession session = request.getSession();
        session.invalidate();

        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("eventId")));
        session.setAttribute("event", event);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}