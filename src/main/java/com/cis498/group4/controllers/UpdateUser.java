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
import java.io.PrintWriter;

/**
 * The UpdateUser servlet responds to requests to edit a user's information.
 */
@WebServlet(name = "UpdateUser", urlPatterns = "/manager/update-user")
public class UpdateUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public UpdateUser() {
        super();
        userData = new UserDataAccess();
    }

    /**
     * Render form to collect new User information. Existing information should pre-populate the fields
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
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

        String url = "/WEB-INF/views/update-user.jsp";
        String pageTitle;
        String back = "list-users";

        User user = userData.getUser(Integer.parseInt(request.getParameter("id")));

        pageTitle = String.format("Edit info for user %s %s", user.getFirstName(), user.getLastName());

        request.setAttribute("user", user);
        request.setAttribute("pageTitle", pageTitle);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Build new User with posted information and submit it for the database. Respond with confirmation message.
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
        // No need for pageTitle or back
        String statusMessage;
        String statusType;

        int status;

        User user = null;

        try {
            user = userData.getUser(Integer.parseInt(request.getParameter("id")));

            status = UserHelpers.setAttributesFromRequest(user, request);

        } catch (Exception e) {
            status = UserHelpers.INVALID_USER;
        }

        // Perform update and respond with appropriate message
        switch(status) {
            case UserHelpers.SUCCESSFUL_WRITE:
                // Attempt write to DB and respond to user
                int updateStatus = userData.updateUser(user);

                if (updateStatus == 0) {
                    statusMessage = "User information updated successfully.";
                    statusType = "success";
                } else if (updateStatus == -1) {
                    statusMessage = "<strong>Error!</strong> Invalid data entered for user update!";
                    statusType = "danger";
                } else {
                    statusMessage = "<strong>Error!</strong> Update operation failed!";
                    statusType = "danger";
                }
                break;
            case UserHelpers.INVALID_DATA:
                statusMessage = "<strong>Error!</strong> Invalid data entered for user!";
                statusType = "danger";
                break;
            case UserHelpers.INVALID_NAME:
                statusMessage = "<strong>Error!</strong> Invalid name entered for user!";
                statusType = "danger";
                break;
            case UserHelpers.INVALID_EMAIL:
                statusMessage = "<strong>Error!</strong> Email address must be in the format name@host!";
                statusType = "danger";
                break;
            default:
                statusMessage = "<strong>Error!</strong> Update user operation failed!";
                statusType = "danger";
                break;
        }

        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
