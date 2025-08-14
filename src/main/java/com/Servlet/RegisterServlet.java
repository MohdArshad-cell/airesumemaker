package com.Servlet;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.DAO.UserDAO;
import com.Db.DBConnect;
import com.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String fullName = req.getParameter("fname");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            // --- Password Hashing (IMPORTANT for security) ---
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            String hashedPassword = sb.toString();
            // --- End of Hashing ---

            User user = new User(fullName, email, hashedPassword);

            // Using our DAO to save the user
            UserDAO dao = new UserDAO(DBConnect.getConn());
            boolean isSuccess = dao.addUser(user);

            HttpSession session = req.getSession();

            if (isSuccess) {
                session.setAttribute("successMsg", "Registration Successful! Please login.");
                resp.sendRedirect("login.jsp");
            } else {
                session.setAttribute("errorMsg", "Something went wrong on the server.");
                resp.sendRedirect("register.jsp");
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // In a real app, you might want a more user-friendly error page
        }
    }
}