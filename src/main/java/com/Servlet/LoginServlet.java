package com.Servlet;

import java.io.IOException;
import java.security.MessageDigest;

import com.DAO.UserDAO;
import com.Db.DBConnect;
import com.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            // Hash the incoming password to match the one in the DB
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            String hashedPassword = sb.toString();

            UserDAO dao = new UserDAO(DBConnect.getConn());
            User user = dao.getUserByEmailAndPassword(email, hashedPassword);

            HttpSession session = req.getSession();

            if (user != null) {
                // Login Success
                session.setAttribute("loggedInUser", user);
                resp.sendRedirect("home.jsp"); // Redirect to the user's dashboard
            } else {
                // Login Failure
                session.setAttribute("errorMsg", "Invalid email or password.");
                resp.sendRedirect("login.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}