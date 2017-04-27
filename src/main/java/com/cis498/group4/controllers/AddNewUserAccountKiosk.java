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
 * The AddNewUserAccountKiosk servlet responds to requests to add a new user from the kiosk.
 */
@WebServlet(name = "AddNewUserAccountKiosk", urlPatterns = "/kiosk/add-new-user-account")
public class AddNewUserAccountKiosk extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public AddNewUserAccountKiosk() {
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
        request.setAttribute("back", request.getContextPath() + "/kiosk");

        response.setHeader("Refresh", "60;url=" + request.getContextPath() + "/kiosk");
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

        String url = "/WEB-INF/views/kiosk-message.jsp";
        String pageTitle;
        String statusMessage;
        String statusType;

        // If passwords do not match, try again
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("pwd-conf");

        if (!password.equals(repeatPassword)) {
            url = "/WEB-INF/views/add-new-user-account.jsp";    // Render same view as console new user
            pageTitle = "Create new user account";
            statusMessage = "<strong>Error!</strong> New password fields do not match!";
            statusType = "danger";
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
                pageTitle = "Created new user";
                statusMessage = "User account created successfully!";
                statusType = "success";
                // Respond using kiosk message page
                request.setAttribute("message1",
                        String.format("A new user account was created for the email address %s", user.getEmail()));
                request.setAttribute("message2","Please sign in with your new credentials.");
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

        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        response.setHeader("Refresh", "60;url=" + request.getContextPath() + "/kiosk");
        view.forward(request, response);

    }

}
