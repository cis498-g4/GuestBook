package com.cis498.group4.controllers;

import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.SessionHelpers;
import com.cis498.group4.util.UserHelpers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * The AddNewUserAccount servlet responds to requests to add a new user external to the management console.
 */
@WebServlet(name = "AddNewUserAccount", urlPatterns = "/manager/add-new-user-account")
public class AddNewUserAccount extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public AddNewUserAccount() {
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

        String url = "/WEB-INF/views/add-new-user-account.jsp";
        String pageTitle = "Create new user account";
        String back = request.getContextPath() + "/manager/";

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
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

        String url = "/WEB-INF/views/manager-login.jsp";
        String pageTitle = "Management Console Login";
        String back = request.getContextPath() + "/manager/";
        String statusMessage;
        String statusType;

        // Create new user with form information
        User user = new User();

        int status = UserHelpers.setAttributesFromRequest(user, request);

        // If passwords do not match, try again
        String password = request.getParameter("password").trim();
        String repeatPassword = request.getParameter("pwd-conf").trim();

        if (!password.equals(repeatPassword)) {
            status = UserHelpers.REPEAT_PASSWORD;
        }

        if (UserHelpers.validatePassword(password)) {
            user.setPassword(password);
        } else {
            status = UserHelpers.INVALID_PASSWORD;
        }

        // Must create type Guest
        if (user.getType() != User.UserType.GUEST) {
            user.setType(User.UserType.GUEST);
        }

        // Perform insert and respond with appropriate message
        switch(status) {
            case UserHelpers.SUCCESSFUL_WRITE:
                int insertStatus = userData.insertUser(user);
                if (insertStatus == 0) {
                    statusMessage = "User account created successfully. Please sign in.";
                    statusType = "success";
                } else if (insertStatus == -1) {
                    statusMessage = "<strong>Error!</strong> Invalid data entered for new user!";
                    statusType = "danger";
                    url = "/WEB-INF/views/add-new-user-account.jsp";
                    pageTitle = "Create new user account";
                } else if (insertStatus == 1062) {
                    statusMessage = "<strong>Error!</strong> A user with that email address already exists!";
                    statusType = "danger";
                    url = "/WEB-INF/views/add-new-user-account.jsp";
                    pageTitle = "Create new user account";
                } else {
                    statusMessage = "<strong>Error!</strong> Add user operation failed!";
                    statusType = "danger";
                    url = "/WEB-INF/views/add-new-user-account.jsp";
                    pageTitle = "Create new user account";
                }
                break;
            case UserHelpers.INVALID_DATA:
                url = "/WEB-INF/views/add-new-user-account.jsp";
                pageTitle = "Create new user account";
                statusMessage = "<strong>Error!</strong> Invalid data entered for new user!";
                statusType = "danger";
                break;
            case UserHelpers.INVALID_NAME:
                url = "/WEB-INF/views/add-new-user-account.jsp";
                pageTitle = "Create new user account";
                statusMessage = "<strong>Error!</strong> Invalid name entered for new user!";
                statusType = "danger";
                break;
            case UserHelpers.INVALID_EMAIL:
                url = "/WEB-INF/views/add-new-user-account.jsp";
                pageTitle = "Create new user account";
                statusMessage = "<strong>Error!</strong> Email address must be in the format name@host!";
                statusType = "danger";
                break;
            case UserHelpers.INVALID_PASSWORD:
                url = "/WEB-INF/views/add-new-user-account.jsp";
                pageTitle = "Create new user account";
                statusMessage = "<strong>Error!</strong> Password must be 40 or fewer letters, numbers, and/or special characters!";
                statusType = "danger";
                break;
            case UserHelpers.REPEAT_PASSWORD:
                url = "/WEB-INF/views/add-new-user-account.jsp";
                pageTitle = "Create new user account";
                statusMessage = "<strong>Error!</strong> Password fields do not match!";
                statusType = "danger";
                String error = "match";
                request.setAttribute("error", error);
                break;
            default:
                url = "/WEB-INF/views/add-new-user-account.jsp";
                pageTitle = "Create new user account";
                statusMessage = "<strong>Error!</strong> Add user operation failed!";
                statusType = "danger";
                break;
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
