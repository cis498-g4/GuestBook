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
import java.time.format.DateTimeFormatter;
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

        // TODO: If event ended in the past, block edit

        String url = "/WEB-INF/views/update-event.jsp";

        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("event", event);

        String startDt = event.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDt = event.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        request.setAttribute("startDt", startDt);
        request.setAttribute("endDt", endDt);

        List<User> organizers = userData.getOrganizers();
        request.setAttribute("organizers", organizers);

        String pageTitle = String.format("Edit info for event %s", event.getName());
        request.setAttribute("pageTitle", pageTitle);

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
        String statusMessage;
        String statusType;

        // Create new event with form information
        String startInput = request.getParameter("start-dt");
        LocalDateTime startDt = LocalDateTime.parse(startInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String endInput = request.getParameter("end-dt");
        LocalDateTime endDt = LocalDateTime.parse(endInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (startDt.isAfter(LocalDateTime.now())) {
            if (startDt.isBefore(endDt)) {

                Event event = new Event();
                event.setId(Integer.parseInt(request.getParameter("id")));
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
                int updateStatus = eventData.updateEvent(event);

                if (updateStatus == 0) {
                    statusMessage = "Event updated successfully.";
                    statusType = "success";
                } else if (updateStatus == -1) {
                    statusMessage = "<strong>Error!</strong> Invalid data entered for new event!";
                    statusType = "danger";
                } else {
                    statusMessage = "<strong>Error!</strong> Update event operation failed!";
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
