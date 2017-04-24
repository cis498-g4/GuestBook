package com.cis498.group4.controllers;

import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.SurveyDataAccess;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.SessionHelpers;
import com.cis498.group4.util.SurveyHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Created by mpm624 on 4/24/17.
 */
@WebServlet(name = "ShowSurveyForm", urlPatterns = "/manager/take-survey")
public class ShowSurveyForm extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SurveyDataAccess surveyData;
    private EventDataAccess eventData;

    public ShowSurveyForm() {
        super();
        surveyData = new SurveyDataAccess();
        eventData = new EventDataAccess();
    }

    /**
     * Render survey form for the given event
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access by non-Guests
        if (!SessionHelpers.checkGuest(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sessionUser");

        String url = "/WEB-INF/views/survey.jsp";

        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("eventId")));
        request.setAttribute("event", event);

        request.setAttribute("questions", SurveyHelpers.QUESTIONS);
        request.setAttribute("responseTypes", SurveyHelpers.RESPONSE_TYPES);
        String[] responses = {"response_01", "response_02", "response_03", "response_04", "response_05", "response_06",
                "response_07", "response_08", "response_09", "response_10"};
        request.setAttribute("responses", responses);

        String pageTitle = String.format("Survey for %s %s", event.getName(),
                event.getStartDateTime().format(DateTimeFormatter.ofPattern("M/d/YYYY")));
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Insert posted survey data into new survey record, update user attendance
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access by non-Guests
        if (!SessionHelpers.checkGuest(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }



    }

}
