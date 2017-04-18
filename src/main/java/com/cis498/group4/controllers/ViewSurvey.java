package com.cis498.group4.controllers;

import com.cis498.group4.data.SurveyDataAccess;
import com.cis498.group4.models.Survey;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * The ViewSurvey servlet responds with the information for the specified survey, including responses.
 */
@WebServlet(name = "ViewSurvey", urlPatterns = "/manager/view-survey")
public class ViewSurvey extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SurveyDataAccess surveyData;

    public ViewSurvey() {
        super();
        surveyData = new SurveyDataAccess();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/WEB-INF/views/view-survey.jsp";

        Survey survey = surveyData.getSurvey(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("survey", survey);

        // Respond with some basic statistics
        BigDecimal average = survey.responseAvg();
        String sentiment ="";

        if (average.doubleValue() < 2.5) {
            sentiment = "very negative";
        }

        if (average.doubleValue() >= 2.5 && average.doubleValue() < 5.0) {
            sentiment = "somewhat negative";
        }

        if (average.doubleValue() >= 5.0 && average.doubleValue() < 7.5) {
            sentiment = "somewhat positive";
        }

        if (average.doubleValue() >= 7.5) {
            sentiment = "very positive";
        }

        request.setAttribute("average", average);
        request.setAttribute("sentiment", sentiment);

        String pageTitle = String.format("Survey for \"%s\" by %s %s", survey.getEvent().getName(),
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
