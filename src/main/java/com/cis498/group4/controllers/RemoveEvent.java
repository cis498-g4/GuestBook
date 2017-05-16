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
            request.setAttribute("statusMessage", "You must be logged in as an organizer to view the requested page.");
            request.setAttribute("statusType", "warning");
            RequestDispatcher view = request.getRequestDispatcher("/manager/login");
            view.forward(request, response);
            return;
        }

        String url = "/WEB-INF/views/remove-event.jsp";
        String pageTitle;
        String back = "list-events";

        // Get event data, redirect to generic error if not found
        try {
            Event event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));
            request.setAttribute("event", event);

            // Throw error if event name is null
            if (event.getName() == null) {
                throw new Exception("Event name null");
            }

            pageTitle = String.format("Remove event %s?", event.getName());

        } catch (Exception e) {
            pageTitle = "Event Not Found";
            url = "/WEB-INF/views/error-generic.jsp";
            String message = "The event you were attempting to remove could not be found.";
            request.setAttribute("message", message);
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
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
        String statusType;

        Event event = null;

        // Remove event if it has not already started, and does not have any registrants
        try {
            event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));

            if (EventHelpers.startsInFuture(event)) {

                int deleteStatus = eventData.deleteEvent(event);

                // Check status code returned by delete operation
                if (deleteStatus == 0) {
                    statusMessage = "Event has been removed";
                    statusType = "success";
                } else if (deleteStatus == 1451) {
                    statusMessage = "<strong>Error!</strong> Cannot remove a event that is associated with surveys or registrations!";
                    statusType = "danger";
                } else {
                    statusMessage = "<strong>Error!</strong> Remove event operation failed!";
                    statusType = "danger";
                }

            } else {
                statusMessage = "<strong>Error!</strong> Cannot remove an event that has already started!";
                statusType = "danger";
            }

        } catch (Exception e) {
            statusMessage = "<strong>Error!</strong> Remove event operation failed!";
            statusType = "danger";
        }
        
        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
