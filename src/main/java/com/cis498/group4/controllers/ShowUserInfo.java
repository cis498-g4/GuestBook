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
 * The ShowUserInfo servlet responds with the information for the specified user.
 */
@WebServlet(name = "ShowUserInfo", urlPatterns = "/manager/show-user-info")
public class ShowUserInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public ShowUserInfo() {
        super();
        userData = new UserDataAccess();
    }

    /**
     * Render view that shows all info about a user
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

        String url = "/WEB-INF/views/show-user-info.jsp";
        String pageTitle;
        String back = "list-users";

        // Get user data, if not found, respond with generic error
        try {
            User user = userData.getUser(Integer.parseInt(request.getParameter("id")));
            request.setAttribute("user", user);

            // Throw error if user email is null
            if (user.getEmail() == null) {
                throw new Exception("User email null");
            }

            pageTitle = String.format("Info for user %s %s", user.getFirstName(), user.getLastName());

        } catch (Exception e) {
            pageTitle = "User Not Found";
            url = "/WEB-INF/views/error-generic.jsp";
            String message = "The user you were attempting to view could not be found.";
            request.setAttribute("message", message);
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
