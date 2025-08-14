<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login - AI Resume Maker</title>
    <%@include file="all_components/head.jsp" %>
</head>
<body>
    <div class="animated-gradient-background"></div>

    <main>
        <%@include file="all_components/navbar.jsp" %>
        
        <div class="container py-5">
            <div class="row">
                <div class="col-md-6 col-lg-5 mx-auto">
                    <div class="content-card">
                        <h3 class="text-center mb-4">User Login</h3>

                        <%-- Display Messages from Session --%>
                        <%
                            String successMsg = (String) session.getAttribute("successMsg");
                            String errorMsg = (String) session.getAttribute("errorMsg");
                            if (successMsg != null) {
                        %>
                                <div class="alert alert-success" role="alert"><%= successMsg %></div>
                        <%
                                session.removeAttribute("successMsg");
                            }
                            if (errorMsg != null) {
                        %>
                                <div class="alert alert-danger" role="alert"><%= errorMsg %></div>
                        <%
                                session.removeAttribute("errorMsg");
                            }
                        %>

                        <form action="login" method="post">
                            <div class="mb-3">
                                <label for="emailInput" class="form-label">Email address</label> 
                                <input type="email" class="form-control" id="emailInput" required name="email" placeholder="name@example.com">
                            </div>
                            <div class="mb-4">
                                <label for="passwordInput" class="form-label">Password</label> 
                                <input type="password" class="form-control" id="passwordInput" required name="password" placeholder="••••••••">
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-futuristic">Secure Login</button>
                            </div>
                        </form>
                        
                        <div class="text-center mt-4">
                           <p class="text-muted">Don't have an account? <a href="register.jsp" style="color: var(--primary-glow);">Register Here</a></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <%@include file="all_components/footer.jsp" %>
</body>
</html>