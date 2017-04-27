package com.cis498.group4.controllers;

import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * The ManagerLogout servlet responds to requests to log out of the management console.
 */
@WebServlet(name = "ManagerLogout", urlPatterns = "/manager/logout")
public class ManagerLogout extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ManagerLogout() {
        super();
    }

    /**
     * Invalidate session and redirect to login screen
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Invalidate session
        HttpSession session = request.getSession();
        session.invalidate();

        // Redirect to login
        String url = "/WEB-INF/views/manager-login.jsp";
        String pageTitle = "Management Console Login";
        String statusMessage = "Successfully logged out";
        String statusType = "success";

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusType", statusType);
        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
