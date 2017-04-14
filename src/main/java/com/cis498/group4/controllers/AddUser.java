package com.cis498.group4.controllers;

import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The AddUser servlet responds to requests to add a new user.
 */
@WebServlet(name = "AddUser", urlPatterns="/manager/add-user")
public class AddUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public AddUser() {
        super();
        userData = new UserDataAccess();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/views/add-user.jsp";
        String pageTitle = "Add new user";
        request.setAttribute("pageTitle", pageTitle);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Create new user with form information
        User user = new User();
        user.setType(User.UserType.valueOf(request.getParameter("type").trim()));
        user.setFirstName(request.getParameter("first-name"));
        user.setLastName(request.getParameter("last-name"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));

        // Attempt write to DB and respond to user
        String url = "/manager/list-users";
        String statusMessage;

        // TODO: Validate user info before commit (http://red.ht/2nMrGNu)
        if (true) {
            int insertStatus = userData.insertUser(user);

            if (insertStatus == 0) {
                statusMessage = "User created successfully.";
            } else {
                statusMessage = "ERROR: Add user operation failed!";
            }

        } else {
            statusMessage = "ERROR: Invalid data entered for new user!";
        }

        request.setAttribute("statusMessage", statusMessage);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

}
