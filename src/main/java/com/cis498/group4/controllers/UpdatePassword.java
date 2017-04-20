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
import java.security.NoSuchAlgorithmException;

/**
 * The UpdatePassword servlet responds to requests to change a user password.
 */
@WebServlet(name = "UpdatePassword", urlPatterns = "/manager/change-password")
public class UpdatePassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public UpdatePassword() {
        super();
        userData = new UserDataAccess();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access if not logged in
        if (!SessionHelpers.checkLogin(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You must login to access this resource");
            return;
        }

        String url = "/WEB-INF/views/change-password.jsp";

        User user = userData.getUser(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("user", user);

        String pageTitle = String.format("Change password for %s %s", user.getFirstName(), user.getLastName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access if not logged in
        if (!SessionHelpers.checkLogin(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You must login to access this resource");
            return;
        }

        String url;
        String pageTitle;
        String statusMessage;
        String error;

        // Create new user with id from form information
        User user = userData.getUser(Integer.parseInt(request.getParameter("id")));

        String oldPassword = request.getParameter("old-password");
        String newPassword = request.getParameter("new-password");
        String repeatPassword = request.getParameter("repeat-password");

        // Old password incorrect? Try again!
        if (!userData.checkPassword(oldPassword, user)) {
            url = String.format("/WEB-INF/views/change-password.jsp?id=%s", user.getId());
            pageTitle = String.format("Change password for %s %s", user.getFirstName(), user.getLastName());
            statusMessage = "ERROR: Old password entered incorrectly!";
            error = "oldpass";
            request.setAttribute("error", error);

        // New passwords don't match? Try again!
        } else if (!newPassword.equals(repeatPassword)) {
            url = String.format("/WEB-INF/views/change-password.jsp?id=%s", user.getId());
            pageTitle = String.format("Change password for %s %s", user.getFirstName(), user.getLastName());
            statusMessage = "ERROR: New password fields do not match!";
            error = "match";
            request.setAttribute("error", error);

        // Attempt write to DB and respond to user
        } else {
            // TODO: Validate password before commit (http://red.ht/2nMrGNu)
            if (true) {
                int updateStatus = userData.updateUserPassword(user, newPassword);

                if (updateStatus == 0) {
                    url = "/manager/view-user";
                    pageTitle = String.format("Info for user %s %s", user.getFirstName(), user.getLastName());
                    statusMessage = "Password updated successfully.";

                } else {
                    url = String.format("/WEB-INF/views/change-password.jsp?id=%s", user.getId());
                    pageTitle = String.format("Change password for %s %s", user.getFirstName(), user.getLastName());
                    statusMessage = "ERROR: Password update failed!";
                    error = "db";
                    request.setAttribute("error", error);

                }

            } else {
                url = String.format("/WEB-INF/views/change-password.jsp?id=%s", user.getId());
                pageTitle = String.format("Change password for %s %s", user.getFirstName(), user.getLastName());
                statusMessage = "ERROR: Invalid data entered for user password!";
                error = "invalid";
                request.setAttribute("error", error);

            }
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("user", user);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
