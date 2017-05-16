package com.cis498.group4.controllers;

import com.cis498.group4.data.SurveyDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Survey;
import com.cis498.group4.models.User;
import com.cis498.group4.util.SessionHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * The ListSurveys servlet responds to requests to view a list of surveys.
 */
@WebServlet(name = "ListSurveys", urlPatterns = "/manager/list-surveys")
public class ListSurveys extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SurveyDataAccess surveyData;

    public ListSurveys() {
        super();
        surveyData = new SurveyDataAccess();
    }

    /**
     * Render a list of completed surveys, with a button to view the details for each
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

        String url = "/WEB-INF/views/list-surveys.jsp";
        String pageTitle = "Surveys";

        // Get survey data from DB
        List<Survey> surveys = surveyData.getAllSurveys();
        request.setAttribute("surveys", surveys);

        request.setAttribute("pageTitle", pageTitle);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
