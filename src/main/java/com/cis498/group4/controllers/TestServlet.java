package com.cis498.group4.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * TestServlet responds with a simple HTTP message and the list of request parameters
 */
@WebServlet(name = "Test", urlPatterns = "/manager/test")
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<html>");
        out.println("<h2>It works!</h2>");
        out.println("<h3>Parameters</h3>");
        out.println("<p>");

        Enumeration<String> params = request.getParameterNames();

        while(params.hasMoreElements()) {
            String paramName = params.nextElement();
            String[] paramValues = request.getParameterValues(paramName);

            out.printf("%s : ", paramName);

            for (int i = 0; i < paramValues.length; i++) {
                out.printf("%s<br>\n", paramValues[i]);
            }
        }

        out.println("</p>");
        out.println("<h3>Headers</h3>");
        out.println("<p>");

        Enumeration<String> headers = request.getHeaderNames();

        while(headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            String headerValue = request.getHeader(headerName);

            out.printf("%s : %s<br>", headerName, headerValue);
        }

        out.println("</p>");
        out.println("</html>");

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
