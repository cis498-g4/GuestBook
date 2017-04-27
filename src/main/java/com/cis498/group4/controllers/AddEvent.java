package com.cis498.group4.controllers;

import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.SessionHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
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
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

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
     * Create a new Event from posted data, write to database, and send confirmation to user
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

        // Create new event with form information

        // TODO: input format must be YYYY-MM-DDTHH:MM - eventually use jQuery for this (http://bit.ly/2ozo93R)
        String startInput = request.getParameter("start-date") + "T" + request.getParameter("start-time");
        LocalDateTime startDt = LocalDateTime.parse(startInput);

        String endInput = request.getParameter("end-date") + "T" + request.getParameter("end-time");
        LocalDateTime endDt = LocalDateTime.parse(endInput);

        if (startDt.isAfter(LocalDateTime.now())) {
            if (startDt.isBefore(endDt)) {

                Event event = new Event();
                event.setName(request.getParameter("name"));
                event.setStartDateTime(startDt);
                event.setEndDateTime(endDt);

                // Assume user ID is valid - chosen from selection box
                User presenter = new User();
                presenter.setId(Integer.parseInt(request.getParameter("pres-id")));
                event.setPresenter(presenter);

                event.setOpenRegistration(request.getParameter("open-reg") != null);

                if (request.getParameter("reg-code") != null && request.getParameter("reg-code").length() > 0) {
                    event.setRegistrationCode(request.getParameter("reg-code"));
                }

                event.setMandatorySurvey(request.getParameter("survey-req") != null);

                if (request.getParameter("capacity") != null && request.getParameter("capacity").length() > 0) {
                    event.setCapacity(Integer.parseInt(request.getParameter("capacity")));
                } else {
                    event.setCapacity(-1);
                }

                if (event.getCapacity() <= 0) {
                    event.setCapacity(-1);
                }

                // Attempt write to DB and respond to event
                int insertStatus = eventData.insertEvent(event);

                if (insertStatus == 0) {
                    statusMessage = "Event created successfully.";
                    statusType = "success";
                } else if (insertStatus == -1) {
                    statusMessage = "<strong>Error!</strong> Invalid data entered for new event!";
                    statusType = "danger";
                } else {
                    statusMessage = "<strong>Error!</strong> Add event operation failed!";
                    statusType = "danger";
                }

            } else {
                statusMessage = "<strong>Error!</strong> Event end time occurs before event start time!";    // TODO move these into insert status check
                statusType = "danger";
            }

        } else {
            statusMessage = "<strong>Error!</strong> Start time occurs in the past!";    // TODO move these into insert status check
            statusType = "danger";
        }

        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
