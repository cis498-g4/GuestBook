package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.SurveyDataAccess;
import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.Survey;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mpm624 on 4/24/17.
 */
@WebServlet(name = "ShowSurveyForm", urlPatterns = "/manager/take-survey")
public class ShowSurveyForm extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SurveyDataAccess surveyData;
    private AttendanceDataAccess attendanceData;

    public ShowSurveyForm() {
        super();
        surveyData = new SurveyDataAccess();
        attendanceData = new AttendanceDataAccess();
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

        Attendance attendance = attendanceData.getAttendance(
                user.getId(), Integer.parseInt(request.getParameter("eventId")));

        Event event = attendance.getEvent();
        request.setAttribute("event", event);

        // Restrict access to surveys if the guest did not sign in to the event
        if (attendance.getStatus() == null || attendance.getStatus() == Attendance.AttendanceStatus.NOT_ATTENDED) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        // Restrict if survey already exists
        if (surveyData.getSurveyId(user, event) > 0) {
            request.setAttribute("surveyTaken", true);
        }

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

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sessionUser");

        String url = "/manager/list-surveys-guest";
        String statusMessage;

        Attendance attendance = attendanceData.getAttendance(user.getId(), Integer.parseInt(request.getParameter("eventId")));

        // Restrict access to surveys if the guest did not sign in to the event
        if (attendance.getStatus() == null || attendance.getStatus() == Attendance.AttendanceStatus.NOT_ATTENDED) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        // Build survey object
        Survey survey = new Survey();

        survey.setUser(user);
        survey.setEvent(attendance.getEvent());
        LocalDateTime submissionDateTime = LocalDateTime.now();
        survey.setSubmissionDateTime(submissionDateTime);

        Map<String, Integer> responses = new HashMap<String, Integer>();

        for(int i = 1; i <= 10; i++) {
            String responseLabel = String.format("response_%02d", i);
            responses.put(responseLabel, Integer.valueOf(request.getParameter(responseLabel)));
        }

        survey.setResponses(responses);

        // Write survey to DB and respond to user
        int insertStatus = surveyData.insertSurvey(survey);

        if (insertStatus == 0) {
            statusMessage = "Thank you for submitting your survey!";

            // Update status if needed
            if (attendance.getStatus() == Attendance.AttendanceStatus.SIGNED_IN) {
                attendanceData.updateStatus(attendance, Attendance.AttendanceStatus.ATTENDED.ordinal());
            }

        } else if (insertStatus == 1062) {
            statusMessage = "ERROR: Survey already submitted for this event!";
        } else if (insertStatus == -1) {
            statusMessage = "ERROR: Invalid data submitted for survey!";
        } else {
            statusMessage = String.format("ERROR: There was a problem submitting your survey (%d)", insertStatus);
        }

        request.setAttribute("statusMessage", statusMessage);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
