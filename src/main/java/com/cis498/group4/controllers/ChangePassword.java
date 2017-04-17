package com.cis498.group4.controllers;

import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.User;
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
 * The ChangePassword servlet responds to requests to change a user password.
 */
@WebServlet(name = "ChangePassword", urlPatterns="/manager/change-password")
public class ChangePassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public ChangePassword() {
        super();
        userData = new UserDataAccess();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/views/change-password.jsp";

        User user = userData.getUser(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("user", user);

        String pageTitle = String.format("Change password for %s %s", user.getFirstName(), user.getLastName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = String.format("/manager/view-user?id=%s", request.getParameter("id"));
        String statusMessage;

        // Create new user with id from form information
        User user = new User();
        user.setId(Integer.parseInt(request.getParameter("id")));

        String newPassword = request.getParameter("new-password");
        String oldPassword = request.getParameter("old-password");

        // Attempt write to DB and respond to user
        if (userData.checkPassword(oldPassword, user)) {
            // TODO: Validate password before commit (http://red.ht/2nMrGNu)
            if (true) {
                int updateStatus = userData.updateUserPassword(user, newPassword);

                if (updateStatus == 0) {
                    statusMessage = "Password updated successfully.";
                } else {
                    statusMessage = "ERROR: Password update failed!";
                }

            } else {
                statusMessage = "ERROR: Invalid data entered for user password!";
            }
        } else {
            url = String.format("/manager/view-user?id=%s", request.getParameter("id"));
            statusMessage = "ERROR: Old password entered incorrectly!";
        }

        request.setAttribute("statusMessage", statusMessage);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
