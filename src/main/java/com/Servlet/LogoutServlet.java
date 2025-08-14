package com.Servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout") // This line is the most important part
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();
            session.removeAttribute("loggedInUser"); // Clear the user's session

            session.setAttribute("successMsg", "Logout Successfully"); // Set a confirmation message
            resp.sendRedirect("login.jsp"); // Redirect to the login page

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}