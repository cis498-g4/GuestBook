package com.cis498.group4.controllers;

import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;

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
 * The EditEvent servlet responds to requests to edit an event's information.
 */
@WebServlet(name = "EditEvent", urlPatterns = "/manager/edit-event")
public class EditEvent extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EventDataAccess eventData;
    private UserDataAccess userData;

    public EditEvent() {
        super();
        eventData = new EventDataAccess();
        userData = new UserDataAccess();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/WEB-INF/views/edit-event.jsp";

        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("event", event);

        List<User> organizers = userData.getOrganizers();
        request.setAttribute("organizers", organizers);

        String pageTitle = String.format("Edit info for event \"%s\"", event.getName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/manager/list-events";
        String statusMessage;

        // Create new event with form information

        // TODO: input format must be YYYY-MM-DDTHH:MM - eventually use jQuery for this (http://bit.ly/2ozo93R)
        String startInput = request.getParameter("start-date") + "T" + request.getParameter("start-time");
        LocalDateTime startDt = LocalDateTime.parse(startInput);

        String endInput = request.getParameter("end-date") + "T" + request.getParameter("end-time");
        LocalDateTime endDt = LocalDateTime.parse(endInput);

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
                // TODO: Validate event info before commit (http://red.ht/2nMrGNu)
                if (true) {

                    int updateStatus = eventData.updateEvent(event);

                    if (updateStatus == 0) {
                        statusMessage = "Event updated successfully.";
                    } else {
                        statusMessage = "ERROR: Add event operation failed!";
                    }

                } else {
                    statusMessage = "ERROR: Invalid data entered for new event!";
                }

            } else {
                statusMessage = "ERROR: Event end time occurs before event start time!";
            }

        } else {
            statusMessage = "ERROR: Start time occurs in the past!";
        }

        request.setAttribute("statusMessage", statusMessage);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
