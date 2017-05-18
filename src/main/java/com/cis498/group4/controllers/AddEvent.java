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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * The AddEvent servlet responds to requests to add a new event.
 */
@WebServlet(name = "AddEvent", urlPatterns = "/manager/add-event")
public class AddEvent extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EventDataAccess eventData;
    private UserDataAccess userData;

    public AddEvent() {
        super();
        eventData = new EventDataAccess();
        userData = new UserDataAccess();
    }

    /**
     * Render a form to collect the necessary data for a new event:
     * name, start/end datetimes, presenter, open registration / survey mandatory flags, registration code, and capacity
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

        // Set params and redirect to add event form
        String url = "/WEB-INF/views/add-event.jsp";
        String pageTitle = "Add new event";

        // Provide a list of event organizers for a select menu, so their names don't need to be entered manually
        List<User> organizers = userData.getOrganizers();
        request.setAttribute("organizers", organizers);

        request.setAttribute("pageTitle", pageTitle);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Create a new Event from posted data, attempt write to database, and send confirmation to user
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
        // Do not need pageTitle and back attributes, get status code for response
        String statusMessage;
        String statusType;

        // Create new event with form information
        Event event = new Event();
        int status = EventHelpers.setAttributesFromRequest(event, request, eventData, userData);

        // Perform update and respond with appropriate message
        switch (status) {
            case EventHelpers.SUCCESSFUL_WRITE:
                int insertStatus = eventData.insertEvent(event);
                if (insertStatus == 0) {
                    statusMessage = "Event created successfully.";
                    statusType = "success";
                } else if (insertStatus == -1) {
                    statusMessage = "<strong>Error!</strong> Invalid data entered for new event!";
                    statusType = "danger";
                } else if (insertStatus == 1062) {
                    statusMessage = String.format("<strong>Error!</strong> The registration code %s is already in use. " +
                            "Please choose a different code.", event.getRegistrationCode());
                    statusType = "danger";
                } else {
                    statusMessage = "<strong>Error!</strong> Add event operation failed!";
                    statusType = "danger";
                }
                break;
            case EventHelpers.INVALID_DATA:
                statusMessage = "<strong>Error!</strong> Invalid data entered for new event!";
                statusType = "danger";
                break;
            case EventHelpers.INVALID_DATE:
                statusMessage = "<strong>Error!</strong> Date must be in the format YYYY-MM-DD HH:MM:SS";
                statusType = "danger";
                break;
            case EventHelpers.START_IN_PAST:
                statusMessage = "<strong>Error!</strong> Start time must not occur in the past!";
                statusType = "danger";
                break;
            case EventHelpers.END_BEFORE_START:
                statusMessage = "<strong>Error!</strong> Event end time occurs before event start time!";
                statusType = "danger";
                break;
            case EventHelpers.CONCLUDED:
                statusMessage = "<strong>Error!</strong> Event time must not occur in the past!";
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
                statusMessage = "<strong>Error!</strong> Add event operation failed!";
                statusType = "danger";
                break;
        }

        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
