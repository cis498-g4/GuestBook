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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * The ShowUserInfoGuest servlet responds with the information for the current guest.
 */
@WebServlet(name = "ShowUserInfoGuest", urlPatterns = "/manager/show-user-info-guest")
public class ShowUserInfoGuest extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public ShowUserInfoGuest() {
        super();
        userData = new UserDataAccess();
    }

    /**
     * Render view that shows all info about the user
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access by non-Guests
        if (!SessionHelpers.checkGuest(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        String url = "/WEB-INF/views/show-user-info-guest.jsp";
        String pageTitle = "Account Information";

        request.setAttribute("pageTitle", pageTitle);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    /**
     * Change user information if they click "update information"
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
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
        int userId = user.getId();

        String url = "/WEB-INF/views/show-user-info-guest.jsp";
        String pageTitle = "Account Information";
        String statusMessage;
        String statusType;

        // Update user with form information
        int status = UserHelpers.setAttributesFromRequest(user, request);

        // Perform update and respond with appropriate message
        switch(status) {
            case UserHelpers.SUCCESSFUL_WRITE:
                int updateStatus = userData.updateUser(user);

                if (updateStatus == 0) {
                    statusMessage = "Information updated successfully.";
                    statusType = "success";

                    // Update session user information as well
                    session.setAttribute("sessionUser", user);

                } else if (updateStatus == -1) {
                    statusMessage = "<strong>Error!</strong> Invalid data entered for update!";
                    statusType = "danger";
                } else {
                    statusMessage = "<strong>Error!</strong> Update operation failed!";
                    statusType = "danger";
                }
                break;
            case UserHelpers.INVALID_DATA:
                statusMessage = "<strong>Error!</strong> Invalid data entered!";
                statusType = "danger";
                break;
            case UserHelpers.INVALID_NAME:
                statusMessage = "<strong>Error!</strong> Invalid name entered!";
                statusType = "danger";
                break;
            case UserHelpers.INVALID_EMAIL:
                statusMessage = "<strong>Error!</strong> Email address must be in the format name@host!";
                statusType = "danger";
                break;
            default:
                statusMessage = "<strong>Error!</strong> Update operation failed!";
                statusType = "danger";
                break;
        }

        // Update session user from database - otherwise, sessionUser gets updated even in cases of failure
        User updatedUser = userData.getUser(userId);
        session.setAttribute("sessionUser", updatedUser);

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
