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
 * The RemoveUser servlet responds to requests to delete a user.
 */
@WebServlet(name = "RemoveUser", urlPatterns = "/manager/remove-user")
public class RemoveUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public RemoveUser() {
        super();
        userData = new UserDataAccess();
    }

    /**
     * Render a confirmation message with the details of the User to be deleted
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

        String url = "/WEB-INF/views/remove-user.jsp";
        String pageTitle;
        String back = "list-users";

        // Get event data, redirect to generic error if not found
        try {
            User user = userData.getUser(Integer.parseInt(request.getParameter("id")));
            request.setAttribute("user", user);

            // Throw error if user email is null
            if (user.getEmail() == null) {
                throw new Exception("User email null");
            }

            pageTitle = String.format("Remove user %s %s?", user.getFirstName(), user.getLastName());

        } catch (Exception e) {
            pageTitle = "User Not Found";
            url = "/WEB-INF/views/error-generic.jsp";
            String message = "The user you were attempting to remove could not be found.";
            request.setAttribute("message", message);
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Receive deletion confirmation, process the deletion in the database, and respond with a confirmation message
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

        // Get current session user to ensure user is not attempting to delete him/herself
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("sessionUser");

        String url = "/manager/list-users";
        String statusMessage;
        String statusType;

        User user = null;

        // Remove user if not current user, and user is not associated with registrations or surveys
        try {
            int userId = Integer.parseInt(request.getParameter("id"));

            // Block users from deleting themselves
            if (userId == sessionUser.getId()) {
                throw new IllegalArgumentException();
            }

            user = userData.getUser(userId);

            int deleteStatus = userData.deleteUser(user);

            if (deleteStatus == 0) {
                statusMessage = "All we are is dust in the wind";
                statusType = "success";
            } else if (deleteStatus == 1451) {
                statusMessage = "<strong>Error!</strong> Cannot remove a user who is associated with one or more events or surveys!";
                statusType = "danger";
            } else {
                statusMessage = "<strong>Error!</strong> Remove user operation failed!";
                statusType = "danger";
            }
        } catch (IllegalArgumentException e) {
            statusMessage = "<strong>Error!</strong> You cannot remove yourself!";
            statusType = "danger";
        } catch (Exception e) {
            statusMessage = "<strong>Error!</strong> Remove user operation failed!";
            statusType = "danger";
        }

        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
