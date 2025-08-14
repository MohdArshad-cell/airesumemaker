<%@ page import="com.entity.User" %>

<%-- 
    This is the FINAL, SELF-CONTAINED navbar. 
    It declares its own 'user' variable from the session.
--%>
<% User user = (User) session.getAttribute("loggedInUser"); %>

<nav class="navbar navbar-expand-lg navbar-dark fixed-top">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="index.jsp"><i class="fas fa-rocket me-2"></i>AI Resume Maker</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-lg-center">
                <% if (user != null) { %>
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="home.jsp">Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="mydocuments.jsp">My Resumes</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fas fa-user-circle me-1"></i> <%= user.getFullName() %>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="profile.jsp">Profile</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="logout">Logout</a></li>
                        </ul>
                    </li>
                <% } else { %>
                    <li class="nav-item">
                        <a class="nav-link" href="login.jsp">Login</a>
                    </li>
                    <li class="nav-item ms-lg-2">
                       <a href="register.jsp" class="btn btn-futuristic btn-sm">Sign Up</a>
                    </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>