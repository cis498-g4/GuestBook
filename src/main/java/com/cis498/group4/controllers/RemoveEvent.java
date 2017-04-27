package com.cis498.group4.controllers;

import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.models.Event;
import com.cis498.group4.util.EventHelpers;
import com.cis498.group4.util.SessionHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * The RemoveEvent servlet responds to requests to delete a event.
 */
@WebServlet(name = "RemoveEvent", urlPatterns = "/manager/remove-event")
public class RemoveEvent extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EventDataAccess eventData;

    public RemoveEvent() {
        super();
        eventData = new EventDataAccess();
    }

    /**
     * Render a confirmation message with the details of the Event to be deleted
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

        String url = "/WEB-INF/views/remove-event.jsp";

        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("event", event);

        String pageTitle = String.format("Delete event \"%s\"?", event.getName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Receive deletion confirmation, process the deletion in the database, and respond with a confirmation message
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

        String url = "/manager/list-events";
        String statusMessage;

        int id = Integer.parseInt(request.getParameter("id"));

        Event event = eventData.getEvent(id);

        if (EventHelpers.startsInFuture(event)) {

            int deleteStatus = eventData.deleteEvent(event);

            // Check status code returned by delete operation
            if (deleteStatus == 0) {
                statusMessage = "Event has been removed";
            } else if (deleteStatus == 1451) {
                statusMessage = "ERROR: Cannot delete a event that is associated with surveys or registrations!";
            } else {
                statusMessage = "ERROR: Delete event operation failed!";
            }

        } else {
            statusMessage = "ERROR: Cannot delete an event that has already started!";
        }

        request.setAttribute("statusMessage", statusMessage);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
