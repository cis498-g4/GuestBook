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
 * The ManagerHome servlet represents the home page for the management console.
 * If the user is logged in, it displays a landing page. Otherwise, it redirects him/her to login.
 */
@WebServlet(name = "ManagerHome", urlPatterns = "/manager/home")
public class ManagerHome extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ManagerHome() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User sessionUser = SessionHelpers.getSessionUser(session);
        String url;

        // If user logged in, direct to landing page, otherwise to login
        if (sessionUser != null) {
            url = "/WEB-INF/views/manager-home.jsp";
            String userType = sessionUser.getType() == User.UserType.ORGANIZER ? "Event Organizer" : "Guest";
            String pageTitle = String.format("%s Management Console", userType);
            request.setAttribute("sessionUser", sessionUser);
            request.setAttribute("pageTitle", pageTitle);

            RequestDispatcher view = request.getRequestDispatcher(url);
            view.forward(request, response);
        } else {
            url = "/manager/login";
            response.sendRedirect(url);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
