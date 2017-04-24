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
import java.util.HashMap;
import java.util.Map;

/**
 * The ShowSurveyInfo servlet responds with the information for the specified survey, including responses.
 */
@WebServlet(name = "ShowSurveyInfo", urlPatterns = "/manager/view-survey")
public class ShowSurveyInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SurveyDataAccess surveyData;

    public ShowSurveyInfo() {
        super();
        surveyData = new SurveyDataAccess();
    }

    /**
     * Render view that shows survey info, responses, and aggregated data (average, sentiment)
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

        String url = "/WEB-INF/views/view-survey.jsp";

        Survey survey = surveyData.getSurvey(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("survey", survey);

        // Set questions and responses
        Map<String, Integer> surveyResponses = SurveyHelpers.getQuestionsResponses(survey);
        request.setAttribute("surveyResponses", surveyResponses);

        // Respond with some basic statistics
        BigDecimal average = SurveyHelpers.responseAverage(survey);
        String sentiment = SurveyHelpers.responseSentiment(average.doubleValue());

        request.setAttribute("average", average);
        request.setAttribute("sentiment", sentiment);

        String pageTitle = String.format("Survey for %s, by %s %s", survey.getEvent().getName(),
                survey.getUser().getFirstName(), survey.getUser().getLastName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
