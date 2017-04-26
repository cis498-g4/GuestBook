package com.cis498.group4.controllers;

import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.SessionHelpers;

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
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", request.getContextPath() + "/manager/");

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

        String statusMessage;

        // If passwords do not match, try again
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("pwd-conf");

        if (!password.equals(repeatPassword)) {
            url = "/WEB-INF/views/add-new-user-account.jsp";
            pageTitle = "Create new user account";
            statusMessage = "ERROR: Password fields do not match!";
            String error = "match";
            request.setAttribute("error", error);

        } else {

            // Create new user with form information
            User user = new User();
            user.setType(User.UserType.GUEST);  // Can only create guests this way, natch
            user.setFirstName(request.getParameter("first-name"));
            user.setLastName(request.getParameter("last-name"));
            user.setEmail(request.getParameter("email"));
            user.setPassword(request.getParameter("password"));

            // Attempt write to DB and respond to user
            int insertStatus = userData.insertUser(user);

            if (insertStatus == 0) {
                statusMessage = "User account created successfully. Please sign in.";
            } else if (insertStatus == -1) {
                statusMessage = "ERROR: Invalid data entered for new user!";
                url = "/WEB-INF/views/add-new-user-account.jsp";
                pageTitle = "Create new user account";
            } else if (insertStatus == 1062) {
                statusMessage = "ERROR: A user with that email address already exists!";
                url = "/WEB-INF/views/add-new-user-account.jsp";
                pageTitle = "Create new user account";
            } else {
                statusMessage = "ERROR: Add user operation failed!";
                url = "/WEB-INF/views/add-new-user-account.jsp";
                pageTitle = "Create new user account";
            }

        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("statusMessage", statusMessage);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
