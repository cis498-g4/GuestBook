package com.cis498.group4.controllers;

import com.cis498.group4.data.SurveyDataAccess;
import com.cis498.group4.models.Survey;
import com.cis498.group4.util.SessionHelpers;
import com.cis498.group4.util.SurveyHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * The ShowSurveyInfo servlet responds with the information for the specified survey, including responses.
 */
@WebServlet(name = "ShowSurveyInfo", urlPatterns = "/manager/show-survey-info")
public class ShowSurveyInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SurveyDataAccess surveyData;

    public ShowSurveyInfo() {
        super();
        surveyData = new SurveyDataAccess();
    }

    /**
     * Render view that shows survey info, responses, and aggregated data (average, sentiment)
     * @param request The HTTP request received from the client
     * @param response The HTTP response returned by the servlet
     * @throws ServletException The request could not be handled
     * @throws IOException An input or output error has occurred
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

        String url = "/WEB-INF/views/show-survey-info.jsp";
        String pageTitle;
        String back = "list-surveys";

        // Get survey data, redirect to generic error if not found
        try {
            Survey survey = surveyData.getSurvey(Integer.parseInt(request.getParameter("id")));
            request.setAttribute("survey", survey);

            // Respond with some basic statistics
            BigDecimal average = SurveyHelpers.responseAverage(survey);
            String sentiment = SurveyHelpers.responseSentiment(average.doubleValue());

            request.setAttribute("average", average);
            request.setAttribute("sentiment", sentiment);

            pageTitle = String.format("Survey for %s by %s %s", survey.getEvent().getName(),
                    survey.getUser().getFirstName(), survey.getUser().getLastName());

        } catch (Exception e) {
            pageTitle = "Survey Not Found";
            url = "/WEB-INF/views/error-generic.jsp";
            String message = "The survey you were attempting to view could not be found.";
            request.setAttribute("message", message);
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
