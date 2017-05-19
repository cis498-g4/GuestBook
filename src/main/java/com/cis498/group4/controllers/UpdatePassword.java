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
import java.io.IOException;

/**
 * The UpdatePassword servlet responds to requests to change a user password.
 * FOR DEMO ONLY: Naturally we should not give organizers the power to edit other user's passwords!
 */
@WebServlet(name = "UpdatePassword", urlPatterns = "/manager/update-password")
public class UpdatePassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public UpdatePassword() {
        super();
        userData = new UserDataAccess();
    }

    /**
     * Render form where user must supply old password and type the new password twice to change
     * @param request The HTTP request received from the client
     * @param response The HTTP response returned by the servlet
     * @throws ServletException The request could not be handled
     * @throws IOException An input or output error has occurred
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

        String url = "/WEB-INF/views/update-password.jsp";
        String pageTitle;
        String back = "list-users";

        // Create form for specified user's password, send generic error if user not found
        try {
            User user = userData.getUser(Integer.parseInt(request.getParameter("id")));
            request.setAttribute("user", user);

            // Throw error if email is null
            if (user.getEmail() == null) {
                throw new Exception("User email null");
            }

            pageTitle = String.format("Change password for %s %s", user.getFirstName(), user.getLastName());
        } catch (Exception e) {
            pageTitle = "User Not Found";
            url = "/WEB-INF/views/error-generic.jsp";
            String message = "The user you were attempting to update could not be found.";
            request.setAttribute("message", message);
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Verify old password and matching new password fields, update the user password in the database,
     * and respond with confirmation.
     * @param request The HTTP request received from the client
     * @param response The HTTP response returned by the servlet
     * @throws ServletException The request could not be handled
     * @throws IOException An input or output error has occurred
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access by non-Organizers
        if (!SessionHelpers.checkOrganizer(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        String url;
        String pageTitle;
        String back = "list-users";
        String statusMessage;
        String statusType;
        String error;

        // Create new user with id from form information
        User user = userData.getUser(Integer.parseInt(request.getParameter("id")));

        String oldPassword = request.getParameter("old-password");
        String newPassword = request.getParameter("new-password");
        String repeatPassword = request.getParameter("repeat-password");

        // Old password incorrect? Try again!
        if (!userData.checkPassword(oldPassword, user)) {
            url = String.format("/WEB-INF/views/update-password.jsp?id=%s", user.getId());
            pageTitle = String.format("Change password for %s %s", user.getFirstName(), user.getLastName());
            statusMessage = "<strong>Error!</strong> Old password entered incorrectly!";
            statusType = "danger";
            error = "oldpass";
            request.setAttribute("error", error);

        // New passwords don't match? Try again!
        } else if (!newPassword.equals(repeatPassword)) {
            url = String.format("/WEB-INF/views/update-password.jsp?id=%s", user.getId());
            pageTitle = String.format("Change password for %s %s", user.getFirstName(), user.getLastName());
            statusMessage = "<strong>Error!</strong> New password fields do not match!";
            statusType = "danger";
            error = "match";
            request.setAttribute("error", error);

        // Attempt write to DB and respond to user
        } else {

            int updateStatus = userData.updateUserPassword(user, newPassword);

            if (updateStatus == 0) {
                url = "/manager/show-user-info";
                pageTitle = String.format("Info for user %s %s", user.getFirstName(), user.getLastName());
                statusMessage = "Password updated successfully.";
                statusType = "success";

            } else if (updateStatus == -1) {
                url = String.format("/WEB-INF/views/update-password.jsp?id=%s", user.getId());
                pageTitle = String.format("Change password for %s %s", user.getFirstName(), user.getLastName());
                statusMessage = "<strong>Error!</strong> Invalid data entered for user password!";
                statusType = "danger";
                error = "invalid";
                request.setAttribute("error", error);

            } else {
                url = String.format("/WEB-INF/views/update-password.jsp?id=%s", user.getId());
                pageTitle = String.format("Change password for %s %s", user.getFirstName(), user.getLastName());
                statusMessage = "<strong>Error!</strong> Password update failed!";
                statusType = "danger";
                error = "db";
                request.setAttribute("error", error);

            }
            
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        request.setAttribute("user", user);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
