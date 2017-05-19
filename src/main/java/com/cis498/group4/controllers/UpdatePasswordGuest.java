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
 * The UpdatePasswordGuest servlet responds to guest requests to change their password.
 */
@WebServlet(name = "UpdatePasswordGuest", urlPatterns = "/manager/update-password-guest")
public class UpdatePasswordGuest extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public UpdatePasswordGuest() {
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

        // Restrict access by non-Guests
        if (!SessionHelpers.checkGuest(request.getSession())) {
            request.setAttribute("statusMessage", "You must be logged in as a guest to view the requested page.");
            request.setAttribute("statusType", "warning");
            RequestDispatcher view = request.getRequestDispatcher("/manager/login");
            view.forward(request, response);
            return;
        }

        String url = "/WEB-INF/views/update-password-guest.jsp";
        String pageTitle = "Update password";
        String back = "show-user-info-guest";

        // Using session data, no attributes to pass to view

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

        // Restrict access by non-Guests
        if (!SessionHelpers.checkGuest(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sessionUser");

        String url;
        String pageTitle;
        String back = "show-user-info-guest";
        String statusMessage;
        String statusType;
        String error;

        String oldPassword = request.getParameter("old-password");
        String newPassword = request.getParameter("new-password");
        String repeatPassword = request.getParameter("repeat-password");

        // Old password incorrect? Try again!
        if (!userData.checkPassword(oldPassword, user)) {
            url = "/WEB-INF/views/update-password-guest.jsp";
            pageTitle = "Update password";
            statusMessage = "<strong>Error!</strong> Old password entered incorrectly!";
            statusType = "danger";
            error = "oldpass";

        // New passwords don't match? Try again!
        } else if (!newPassword.equals(repeatPassword)) {
            url = "/WEB-INF/views/update-password-guest.jsp";
            pageTitle = "Update password";
            statusMessage = "<strong>Error!</strong> New password fields do not match!";
            statusType = "danger";
            error = "match";

        // Attempt write to DB and respond to user
        } else {

            int updateStatus = userData.updateUserPassword(user, newPassword);

            if (updateStatus == 0) {
                url = "/WEB-INF/views/show-user-info-guest.jsp";
                pageTitle = "Account Information";
                statusMessage = "Password updated successfully.";
                statusType = "success";
                error = "";

            } else if (updateStatus == -1) {
                url = "/WEB-INF/views/update-password-guest.jsp";
                pageTitle = "Update password";
                statusMessage = "<strong>Error!</strong> Invalid data entered for user password!";
                statusType = "danger";
                error = "invalid";

            } else {
                url = "/WEB-INF/views/update-password-guest.jsp";
                pageTitle = "Update password";
                statusMessage = "<strong>Error!</strong> Password update failed!";
                statusType = "danger";
                error = "db";

            }

        }

        request.setAttribute("error", error);
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        request.setAttribute("user", user);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
