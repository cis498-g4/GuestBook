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
import java.sql.SQLException;
import java.util.List;

/**
 * The ListUsers servlet responds to requests to view a list of users.
 */
@WebServlet(name = "ListUsers", urlPatterns = "/manager/list-users")
public class ListUsers extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDataAccess userData;

    public ListUsers() {
        super();
        userData = new UserDataAccess();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = "/WEB-INF/views/list-users.jsp";

        List<User> users = userData.getAllUsers();
        request.setAttribute("users", users);

        String pageTitle = "Users";
        request.setAttribute("pageTitle", pageTitle);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
