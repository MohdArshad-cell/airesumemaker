<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.User, java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%-- Security Check --%>
<%
    if (session.getAttribute("loggedInUser") == null) {
        session.setAttribute("errorMsg", "Please login to continue.");
        response.sendRedirect("login.jsp");
        return; 
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>ATS Score & Checker - AI Resume Maker</title>
    <%@include file="all_components/head.jsp" %>
    <style>
        .workspace-textarea { height: 300px; font-size: 0.9rem; }
        .score-circle { width: 150px; height: 150px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 1rem auto; }
        .score-text { color: white; font-size: 2.5rem; font-weight: 700; }
        .nav-tabs .nav-link { background: transparent; border: 1px solid transparent; color: var(--text-muted); }
        .nav-tabs .nav-link.active { background: var(--card-bg); color: var(--primary-glow); border-color: var(--card-border); border-bottom-color: var(--card-bg); }
        .keyword-list { list-style-type: none; padding-left: 0; }
        .keyword-list li { background: rgba(0,0,0,0.2); padding: 0.5rem 1rem; border-radius: 5px; margin-bottom: 0.5rem; }
        .password-toggle {
            position: absolute;
            top: 50%;
            right: 15px;
            transform: translateY(-50%);
            cursor: pointer;
            color: var(--text-muted);
        }
    </style>
</head>
<body>
    <div class="background-container"><div class="aurora aurora-1"></div><div class="aurora aurora-2"></div></div>
    <main>
        <%@include file="all_components/navbar.jsp" %>
        <div class="container-fluid p-4">
            <div class="row g-4">
                <div class="col-lg-5">
                    <div class="content-card h-100">
                        <h4 class="mb-4"><i class="fas fa-search me-2"></i>ATS Analyzer</h4>
                        <form id="ats-form" action="checkATS" method="post">
                            <div class="mb-3">
                                <label for="resumeContent" class="form-label fw-bold">1. Your Resume Text:</label>
                                <textarea class="form-control workspace-textarea" id="resumeContent" name="resumeContent" placeholder="Paste your resume content here...">${fn:escapeXml(submittedResumeContent)}</textarea>
                            </div>
                             <div class="mb-3">
                                <label for="jobDescription" class="form-label fw-bold">2. Target Job Description:</label>
                                <textarea class="form-control workspace-textarea" id="jobDescription" name="jobDescription" placeholder="Paste the job description here...">${fn:escapeXml(submittedJobDescription)}</textarea>
                            </div>
                            
                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" class="btn btn-futuristic btn-lg" id="analyze-btn">
                                    <span class="btn-text"><i class="fas fa-cogs me-2"></i>Analyze Resume</span>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="col-lg-7">
                    <div id="results-panel" class="content-card h-100">
                        <h4 class="mb-4"><i class="fas fa-chart-bar me-2"></i>Analysis Report</h4>
                        
                        <%-- This JSP block checks if there is result data from the servlet --%>
                        <% if (request.getAttribute("atsScore") == null) { %>
                            
                            <%-- If NO data, show the placeholder --%>
                            <div class="text-center text-muted h-100 d-flex flex-column justify-content-center align-items-center">
                               <i class="fas fa-robot fa-3x mb-3"></i>
                               <h4>Your report will appear here.</h4>
                               <p>Submit your resume and a job description to begin.</p>
                           </div>

                        <% } else { %>
                        
                            <%-- If there IS data, show the results --%>
                            <div class="row">
                                <div class="col-md-4 text-center">
                                    <% int score = (int) request.getAttribute("atsScore"); %>
                                    <div class="score-circle" style="background: conic-gradient(var(--primary-glow) <%= score %>%, #333 <%= score %>%)">
                                        <span class="score-text"><%= score %>%</span>
                                    </div>
                                    <h6 class="fw-bold"><%= score >= 80 ? "Strong Match" : (score >= 60 ? "Good Match" : "Needs Improvement") %></h6>
                                </div>
                                <div class="col-md-8">
                                    <h5>AI Suggestions</h5>
                                    <p class="text-muted">${aiFeedback}</p>
                                    <hr>
                                    <h6>Keywords to Add</h6>
                                    <ul class="keyword-list">
                                        <% for(String keyword : (List<String>)request.getAttribute("missingKeywords")) { %>
                                            <li><i class="fas fa-times-circle text-danger me-2"></i><%= keyword %></li>
                                        <% } %>
                                    </ul>
                                </div>
                            </div>
                            
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <%@include file="all_components/footer.jsp" %>
    <script>
    
const toggleApiKey = document.getElementById('toggleApiKey');
const apiKeyInput = document.getElementById('apiKey');

if (toggleApiKey) {
    toggleApiKey.addEventListener('click', function() {
        const type = apiKeyInput.getAttribute('type') === 'password' ? 'text' : 'password';
        apiKeyInput.setAttribute('type', type);
        this.classList.toggle('fa-eye');
        this.classList.toggle('fa-eye-slash');
    });
}</script>
</body>
</html>