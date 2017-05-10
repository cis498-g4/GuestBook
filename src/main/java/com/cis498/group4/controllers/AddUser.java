package com.cis498.group4.controllers;

import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.User;
import com.cis498.group4.util.SessionHelpers;
import com.cis498.group4.util.UserHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The AddUser servlet responds to requests to add a new user.
 */
@WebServlet(name = "AddUser", urlPatterns = "/manager/add-user")
public class AddUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public AddUser() {
        super();
        userData = new UserDataAccess();
    }

    /**
     * Render a form to collect the necessary data for a new user: first/last name, email, and password
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

        String url = "/WEB-INF/views/add-user.jsp";
        String pageTitle = "Add new user";
        String back = "list-users";

        request.setAttribute("pageTitle", pageTitle);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Create a new User from posted data, write to database, and send confirmation to user
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

        String url = "/manager/list-users";
        String pageTitle;
        String back;
        String statusMessage;
        String statusType;

        // Create new user with form information
        User user = new User();

        int status = UserHelpers.setAttributesFromRequest(user, request);

        String password = request.getParameter("password").trim();
        String repeatPassword = request.getParameter("pwd-conf").trim();

        // New passwords don't match? Try again!
        if (!password.equals(repeatPassword)) {
            status = UserHelpers.REPEAT_PASSWORD;
        }

        if (UserHelpers.validatePassword(password)) {
            user.setPassword(password);
        } else {
            status = UserHelpers.INVALID_PASSWORD;
        }

        // Perform insert and respond with appropriate message
        switch(status) {
            case UserHelpers.SUCCESSFUL_WRITE:
                int insertStatus = userData.insertUser(user);
                if (insertStatus == 0) {
                    statusMessage = "User created successfully.";
                    statusType = "success";
                } else if (insertStatus == -1) {
                    statusMessage = "<strong>Error!</strong> Invalid data entered for new user!";
                    statusType = "danger";
                } else if (insertStatus == 1062) {
                    statusMessage = "<strong>Error!</strong> A user with that email address already exists!";
                    statusType = "danger";
                } else {
                    statusMessage = "<strong>Error!</strong> Add user operation failed!";
                    statusType = "danger";
                }
                break;
            case UserHelpers.INVALID_DATA:
                statusMessage = "<strong>Error!</strong> Invalid data entered for new user!";
                statusType = "danger";
                break;
            case UserHelpers.INVALID_NAME:
                statusMessage = "<strong>Error!</strong> Invalid name entered for new user!";
                statusType = "danger";
                break;
            case UserHelpers.INVALID_EMAIL:
                statusMessage = "<strong>Error!</strong> Email address must be in the format name@host!";
                statusType = "danger";
                break;
            case UserHelpers.INVALID_PASSWORD:
                statusMessage = "<strong>Error!</strong> Password must be 40 or fewer letters, numbers, and/or special characters!";
                statusType = "danger";
                break;
            case UserHelpers.REPEAT_PASSWORD:
                url = "/WEB-INF/views/add-user.jsp";
                pageTitle = "Add New User";
                back = "list-users";
                statusMessage = "<strong>Error!</strong> New password fields do not match!";
                statusType = "danger";
                String error = "match";

                request.setAttribute("pageTitle", pageTitle);
                request.setAttribute("back", back);
                request.setAttribute("error", error);
                break;
            default:
                statusMessage = "<strong>Error!</strong> Add user operation failed!";
                statusType = "danger";
                break;
        }

        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
