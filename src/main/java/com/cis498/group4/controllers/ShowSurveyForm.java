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
import java.util.TreeMap;

/**
 * The ShowSurveyForm servlet displays the survey form and reads the responses for insertion into the database.
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
        String pageTitle;
        String back = "list-surveys-guest";

        try {
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

            pageTitle = String.format("Survey for %s %s", event.getName(),
                    event.getStartDateTime().format(DateTimeFormatter.ofPattern("M/d/YY")));
        } catch (Exception e) {
            url = "/WEB-INF/views/error-generic.jsp";
            pageTitle = "Survey Not Found";
            String message = "There was an error accessing the survey. Please try again.";
            request.setAttribute("message", message);
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
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
        String statusType;

        try {
            Attendance attendance = attendanceData.getAttendance(user.getId(), Integer.parseInt(request.getParameter("eventId")));

            // Build survey object
            Survey survey = new Survey();

            int status = SurveyHelpers.setAttributesFromRequest(survey, user, attendance, request);

            // Perform insert and respond with appropriate message
            switch (status) {
                case SurveyHelpers.SUCCESSFUL_SUBMISSION:
                    int insertStatus = surveyData.insertSurvey(survey);
                    if (insertStatus == 0) {
                        statusMessage = "Thank you for submitting your survey!";
                        statusType = "success";

                        // Update status if needed
                        if (attendance.getStatus() == Attendance.AttendanceStatus.SIGNED_IN) {
                            attendanceData.updateStatus(attendance, Attendance.AttendanceStatus.ATTENDED.ordinal());
                        }
                    } else if (insertStatus == 1062) {
                        statusMessage = "<strong>Error!</strong> Survey already submitted for this event!";
                        statusType = "danger";
                    } else if (insertStatus == -1) {
                        statusMessage = "<strong>Error!</strong> Invalid data submitted for survey!";
                        statusType = "danger";
                    } else {
                        statusMessage = "<strong>Error!</strong> There was a problem submitting your survey.";
                        statusType = "danger";
                    }
                    break;
                case SurveyHelpers.INVALID_USER:
                    statusMessage = "<strong>Error!</strong> Invalid user data for survey. Please try again.";
                    statusType = "danger";
                    break;
                case SurveyHelpers.INVALID_USER_TYPE:
                    statusMessage = "<strong>Error!</strong> Only users of type Guest may submit a survey.";
                    statusType = "danger";
                    break;
                case SurveyHelpers.INVALID_EVENT:
                    statusMessage = "<strong>Error!</strong> Invalid event data for survey. Please try again.";
                    statusType = "danger";
                    break;
                case SurveyHelpers.INVALID_EVENT_DATE:
                    statusMessage = "<strong>Error!</strong> Cannot submit a survey before the event has ended.";
                    statusType = "danger";
                    break;
                case SurveyHelpers.INVALID_ATTENDANCE:
                    statusMessage = "<strong>Error!</strong> Cannot submit a survey for an event if you have not signed in.";
                    statusType = "danger";
                    break;
                case SurveyHelpers.INVALID_RESPONSE:
                    statusMessage = "<strong>Error!</strong> One or more of your responses was invalid or missing.";
                    statusType = "danger";
                    break;
                case SurveyHelpers.INVALID_DATA:
                    statusMessage = "<strong>Error!</strong> Invalid data submitted for survey!";
                    statusType = "danger";
                    break;
                default:
                    statusMessage = "<strong>Error!</strong> Invalid data submitted for survey!";
                    statusType = "danger";
                    break;
            }
        } catch (Exception e) {
            statusMessage = "<strong>Error!</strong> Invalid data submitted for survey!";
            statusType = "danger";
        }

        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
