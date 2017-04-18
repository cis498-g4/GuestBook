package com.cis498.group4.controllers;

import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.models.Event;
import com.cis498.group4.util.EventHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * The DeleteEvent servlet responds to requests to delete a event.
 */
@WebServlet(name = "DeleteEvent", urlPatterns = "/manager/delete-event")
public class DeleteEvent extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EventDataAccess eventData;

    public DeleteEvent() {
        super();
        eventData = new EventDataAccess();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/WEB-INF/views/delete-event.jsp";

        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("event", event);

        String pageTitle = String.format("Delete event \"%s\"?", event.getName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/manager/list-events";
        String statusMessage;

        int id = Integer.parseInt(request.getParameter("id"));

        Event event = eventData.getEvent(id);

        if (event.getStartDateTime().isAfter(LocalDateTime.now())) {

            int deleteStatus = eventData.deleteEvent(id);

            if (deleteStatus == 0) {
                statusMessage = "Event has been removed";
            } else if (deleteStatus == 1451) {
                statusMessage = "ERROR: Cannot delete a event that is associated with surveys or registrations!";
            } else {
                statusMessage = "ERROR: Delete event operation failed!";
            }

        } else {
            statusMessage = "ERROR: Cannot delete an event that has already occurred!";
        }

        request.setAttribute("statusMessage", statusMessage);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
