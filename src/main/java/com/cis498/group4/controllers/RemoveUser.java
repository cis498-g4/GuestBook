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

        String url = "/WEB-INF/views/remove-user.jsp";

        User user = userData.getUser(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("user", user);

        String pageTitle = String.format("Remove user %s %s?", user.getFirstName(), user.getLastName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Receive deletion confirmation, process the deletion in the database, and respond with a confirmation message
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

        User user = userData.getUser(Integer.parseInt(request.getParameter("id")));

        int deleteStatus = userData.deleteUser(user);

        if (deleteStatus == 0) {
            statusMessage = "All we are is dust in the wind";
        } else if (deleteStatus == 1451) {
            statusMessage = "ERROR: Cannot remove a user who is associated with one or more events or surveys!";
        } else {
            statusMessage = "ERROR: Remove user operation failed!";
        }

        request.setAttribute("statusMessage", statusMessage);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
