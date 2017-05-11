package com.cis498.group4.controllers;

import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.EventHelpers;
import com.cis498.group4.util.SessionHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * The UpdateEvent servlet responds to requests to edit an event's information.
 */
@WebServlet(name = "UpdateEvent", urlPatterns = "/manager/update-event")
public class UpdateEvent extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EventDataAccess eventData;
    private UserDataAccess userData;

    public UpdateEvent() {
        super();
        eventData = new EventDataAccess();
        userData = new UserDataAccess();
    }

    /**
     * Render form to collect new Event information. Existing information should pre-populate the fields
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

        String url = "/WEB-INF/views/update-event.jsp";
        String back = "list-events";
        String pageTitle;
        String statusMessage;
        String statusType;

        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));

        String startDt = event.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDt = event.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // If event ended in the past, warn and block editing capability
        if (EventHelpers.endedInPast(event)) {
            statusMessage = "This event occurred in the past, and cannot be updated.";
            statusType = "danger";
            request.setAttribute("concluded", true);
            request.setAttribute("statusMessage", statusMessage);
            request.setAttribute("statusType", statusType);
        }

        List<User> organizers = userData.getOrganizers();

        pageTitle = String.format("Edit info for event %s", event.getName());

        request.setAttribute("event", event);
        request.setAttribute("organizers", organizers);
        request.setAttribute("startDt", startDt);
        request.setAttribute("endDt", endDt);

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Build new Event with posted information and submit it for the database. Respond with confirmation message.
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
        // Do not need pageTitle and back attributes
        String statusMessage;
        String statusType;

        int status;

        Event event = null;

        try {
            event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));

            if (EventHelpers.endedInPast(event)) {
                // If event ended in the past, block edit
                status = EventHelpers.CONCLUDED;
            } else {
                // Add form information to event
                status = EventHelpers.setAttributesFromRequest(event, request, eventData, userData);
            }
        } catch (Exception e) {
            status = EventHelpers.INVALID_EVENT;
        }

        // Perform update and respond with appropriate message
        switch (status) {
            case EventHelpers.SUCCESSFUL_WRITE:
                int updateStatus = eventData.updateEvent(event);
                if (updateStatus == 0) {
                    statusMessage = "Event updated successfully.";
                    statusType = "success";
                } else if (updateStatus == -1) {
                    statusMessage = "<strong>Error!</strong> Invalid data entered for event!";
                    statusType = "danger";
                } else {
                    statusMessage = "<strong>Error!</strong> Update event operation failed!";
                    statusType = "danger";
                }
                break;
            case EventHelpers.INVALID_DATA:
                statusMessage = "<strong>Error!</strong> Invalid data entered for event!";
                statusType = "danger";
                break;
            case EventHelpers.INVALID_DATE:
                statusMessage = "<strong>Error!</strong> Date must be in the format YYYY-MM-DD HH:MM:SS";
                statusType = "danger";
                break;
            case EventHelpers.START_IN_PAST:
                statusMessage = "<strong>Error!</strong> Updated start time must not occur in the past!";
                statusType = "danger";
                break;
            case EventHelpers.END_BEFORE_START:
                statusMessage = "<strong>Error!</strong> Event end time occurs before event start time!";
                statusType = "danger";
                break;
            case EventHelpers.CONCLUDED:
                statusMessage = "<strong>Error!</strong> Cannot update an event that has concluded!";
                statusType = "danger";
                break;
            case EventHelpers.INVALID_PRESENTER:
                statusMessage = "<strong>Error!</strong> Selected presenter not found!";
                statusType = "danger";
                break;
            case EventHelpers.OVERLAPPING_PRESENTER:
                statusMessage = "<strong>Error!</strong> This event overlaps with another event by the same presenter!";
                statusType = "danger";
                break;
            case EventHelpers.INVALID_CAPACITY:
                statusMessage = "<strong>Error!</strong> Capacity must be an integer between 1 and 1000!";
                statusType = "danger";
                break;
            case EventHelpers.INVALID_CODE:
                statusMessage = "<strong>Error!</strong> Registration code must be a string of exactly eight letters and/or numbers!";
                statusType = "danger";
                break;
            default:
                statusMessage = "<strong>Error!</strong> Update event operation failed!";
                statusType = "danger";
                break;
        }

        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
