<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.User, com.DAO.ResumeDAO, com.Db.DBConnect, java.util.List, com.entity.Resume" %>

<%-- 
    CORRECTED LOGIC BLOCK:
    All Java logic is now at the top of the page.
    The 'user' variable is declared here, and the resume list is fetched immediately.
--%>
<%
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        session.setAttribute("errorMsg", "Please login to access the dashboard.");
        response.sendRedirect("login.jsp");
        return; 
    }
    
    // Fetch resumes immediately after verifying the user
    ResumeDAO dao = new ResumeDAO(DBConnect.getConn());
    List<Resume> resumeList = dao.getResumesByUserId(user.getUserId()); 
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>My Documents - AI Resume Maker</title>
    <%@include file="all_components/head.jsp" %>
    <style>
        .document-card {
            display: flex;
            flex-direction: column;
        }
        .document-card .card-footer {
            margin-top: auto; /* Pushes footer to the bottom */
            background: transparent;
            border-top: 1px solid var(--card-border);
        }
    </style>
</head>
<body>
    <div class="background-container">
        <div class="aurora aurora-1"></div>
        <div class="aurora aurora-2"></div>
    </div>

    <main>
        <%-- Use the consistent, dynamic include for the navbar --%>
        <jsp:include page="all_components/navbar.jsp" />
        
        <div class="container py-5">
            <div class="row mb-4 align-items-center">
                <div class="col-md-6">
                    <h1 class="display-5">My Document Hub</h1>
                    <p class="text-muted">Manage all your saved resumes and cover letters.</p>
                </div>
                <div class="col-md-6 text-md-end">
                    <a href="home.jsp" class="btn btn-futuristic"><i class="fas fa-plus me-2"></i>Create New</a>
                </div>
            </div>

            <div class="row g-4">
                
                <%-- The display logic remains the same, using the 'resumeList' variable fetched at the top --%>
                <% if (resumeList.isEmpty()) { %>
                
                    <div class="col-12">
                        <div class="content-card text-center py-5">
                            <i class="fas fa-folder-open fa-3x text-muted mb-3"></i>
                            <h4>You have no saved documents yet.</h4>
                            <p class="text-muted">Go to the dashboard to create one.</p>
                        </div>
                    </div>

                <% } else { %>
                
                    <% for (Resume r : resumeList) { %>
                        <div class="col-lg-4 col-md-6">
                            <div class="content-card h-100 document-card">
                                <div class="card-body">
                                   <h5 class="fw-bold"><i class="fas fa-file-alt me-2"></i><%= r.getTitle() %></h5>
                                   <p class="text-muted">Last updated: <%= r.getCreatedAt().toString().substring(0, 10) %></p>
                                </div>
                                <div class="card-footer">
                                     <a href="editresume.jsp?id=<%= r.getResumeId() %>" class="btn btn-outline-light btn-sm">Edit</a>
                                     <a href="download?id=<%= r.getResumeId() %>" class="btn btn-outline-primary btn-sm">Download</a>
                                     <a href="deleteResume?id=<%=r.getResumeId()%>"
                                        class="btn btn-outline-danger btn-sm"
                                        onclick="return confirm('Are you sure you want to delete this resume?');">Delete</a>
                                </div>
                            </div>
                        </div>
                    <% } %>
                    
                <% } %>

            </div>
        </div>
    </main>

    <%@include file="all_components/footer.jsp" %>
</body>
</html>