package com.cis498.group4.controllers;

import com.cis498.group4.data.UserDataAccess;
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
 * The ManagerLogin servlet responds to login requests for the management console.
 */
@WebServlet(name = "ManagerLogin", urlPatterns = "/manager/login")
public class ManagerLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public ManagerLogin() {
        super();
        userData = new UserDataAccess();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url;
        String pageTitle;

        HttpSession session = request.getSession();
        User sessionUser = SessionHelpers.getSessionUser(session);

        // If user logged in, direct to home, otherwise to login form
        if (sessionUser != null) {
            url = "/manager/home";
            String userType = sessionUser.getType() == User.UserType.ORGANIZER ? "Event Organizer" : "Guest";
            pageTitle = String.format("%s Management Console", userType);

        } else {
            url = "/WEB-INF/views/manager-login.jsp";
            pageTitle = "User login";
        }

        request.setAttribute("pageTitle", pageTitle);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url;
        String pageTitle;
        String statusMessage;
        String error;

        HttpSession session = request.getSession();

        // Get user by email. If not found, incorrect email, try again
        User user = userData.getUserByEmail(request.getParameter("email"));

        if (user == null) {
            url = "/WEB-INF/views/manager-login.jsp";
            pageTitle = "Retry login";
            statusMessage = "ERROR: Incorrect email address!";
            error = "email";
            request.setAttribute("statusMessage", statusMessage);
            request.setAttribute("error", error);

        } else {
            // Check password. If correct, set session user and forward home, if incorrect, try again
            String password = request.getParameter("password");

            if (userData.checkPassword(password, user)) {
                SessionHelpers.setSessionUser(session, user);

                url = "/manager/home";
                String userType = user.getType() == User.UserType.ORGANIZER ? "Event Organizer" : "Guest";
                pageTitle = String.format("%s Management Console", userType);

            } else {
                url = "/WEB-INF/views/manager-login.jsp";
                pageTitle = "Retry login";
                statusMessage = "ERROR: Incorrect password!";
                error = "password";
                request.setAttribute("statusMessage", statusMessage);
                request.setAttribute("error", error);

            }
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("user", user);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
