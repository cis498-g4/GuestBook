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

/**
 * The DeleteUser servlet responds to requests to delete a user.
 */
@WebServlet(name = "DeleteUser", urlPatterns = "/manager/delete-user")
public class DeleteUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public DeleteUser() {
        super();
        userData = new UserDataAccess();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/views/delete-user.jsp";

        User user = userData.getUser(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("user", user);

        String pageTitle = String.format("Delete user %s %s?", user.getFirstName(), user.getLastName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int deleteStatus = userData.deleteUser(Integer.parseInt(request.getParameter("id")));

        String url = "/manager/list-users";
        String statusMessage;

        if (deleteStatus == 0) {
            statusMessage = "All we are is dust in the wind";
        } else if (deleteStatus == 1451) {
            statusMessage = "ERROR: Cannot delete a user who is associated with one or more events or surveys!";
        } else {
            statusMessage = "ERROR: Delete user operation failed!";
        }

        request.setAttribute("statusMessage", statusMessage);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }
}
