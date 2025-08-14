package com.Servlet;

import java.io.IOException;
import java.util.List;

import com.DAO.ResumeDAO;
import com.Db.DBConnect;
import com.entity.Resume;
import com.entity.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/mydocuments")
public class MyDocumentServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            ResumeDAO dao = new ResumeDAO(DBConnect.getConn());
            List<Resume> resumes = dao.getResumesByUserId(user.getUserId());
            request.setAttribute("resumes", resumes);
            RequestDispatcher dispatcher = request.getRequestDispatcher("mydocument.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
