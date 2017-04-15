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
import java.io.PrintWriter;

/**
 * The EditUser servlet responds to requests to edit a user's information.
 */
@WebServlet(name = "EditUser", urlPatterns = "/manager/edit-user")
public class EditUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public EditUser() {
        super();
        userData = new UserDataAccess();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/views/edit-user.jsp";

        User user = userData.getUser(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("user", user);

        String pageTitle = String.format("Edit info for user %s %s", user.getFirstName(), user.getLastName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
        // TODO: Validate user info before commit (http://red.ht/2nMrGNu)
        if (true) {
            int updateStatus = userData.updateUser(user);

            if (updateStatus == 0) {
                statusMessage = "User information updated successfully.";
            } else {
                statusMessage = "ERROR: Update operation failed!";
            }

        } else {
            statusMessage = "ERROR: Invalid data entered for user update!";
        }

        request.setAttribute("statusMessage", statusMessage);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }
    
}
