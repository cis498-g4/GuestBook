package com.cis498.group4.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The ListGuests class handles requests to view a list of event guests.
 */
@WebServlet(urlPatterns = "/manager/list-guests")
public class ListGuests extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ListGuests() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
