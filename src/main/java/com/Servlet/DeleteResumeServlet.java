package com.Servlet;

import java.io.IOException;

import com.DAO.ResumeDAO;
import com.Db.DBConnect;
import com.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/deleteResume")
public class DeleteResumeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Get the resume ID from the URL parameter (e.g., deleteResume?id=5)
            int resumeId = Integer.parseInt(req.getParameter("id"));
            
            // Get the logged-in user from the session for a security check
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("loggedInUser");
            
            if (user != null) {
                ResumeDAO dao = new ResumeDAO(DBConnect.getConn());
                
                // Pass both the resumeId and userId to the DAO
                // This ensures a user can only delete a resume that they own
                boolean f = dao.deleteResume(resumeId, user.getUserId()); 
                
                if (f) {
                    session.setAttribute("successMsg", "Resume deleted successfully.");
                } else {
                    session.setAttribute("errorMsg", "Error: Could not delete resume. You may not be the owner.");
                }
            } else {
                 session.setAttribute("errorMsg", "You must be logged in to perform this action.");
            }
            
            // Redirect back to the documents page to see the updated list
            resp.sendRedirect("mydocuments.jsp");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}