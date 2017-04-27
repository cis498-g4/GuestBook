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
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        String url = "/WEB-INF/views/update-user.jsp";

        User user = userData.getUser(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("user", user);

        String pageTitle = String.format("Edit info for user %s %s", user.getFirstName(), user.getLastName());
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
        String statusMessage;

        // Create new user with form information
        User user = new User();
        user.setId(Integer.parseInt(request.getParameter("id")));
        user.setType(User.UserType.valueOf(request.getParameter("type").trim()));
        user.setFirstName(request.getParameter("first-name"));
        user.setLastName(request.getParameter("last-name"));
        user.setEmail(request.getParameter("email"));

        // Attempt write to DB and respond to user
        int updateStatus = userData.updateUser(user);

        if (updateStatus == 0) {
            statusMessage = "User information updated successfully.";
        } else if (updateStatus == -1) {
            statusMessage = "ERROR: Invalid data entered for user update!";
        } else {
            statusMessage = "ERROR: Update operation failed!";
        }

        request.setAttribute("statusMessage", statusMessage);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
