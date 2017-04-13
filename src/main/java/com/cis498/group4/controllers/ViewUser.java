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
import java.util.List;

/**
 * The ViewUSer servlet responds with the information for the specified user.
 */
@WebServlet(name = "ViewUser", urlPatterns = "/manager/view-user")
public class ViewUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public ViewUser() {
        super();
        userData = new UserDataAccess();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/views/view-user.jsp";

        User user = userData.getUser(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("user", user);

        String pageTitle = String.format("Info for user %s %s", user.getFirstName(), user.getLastName());
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}