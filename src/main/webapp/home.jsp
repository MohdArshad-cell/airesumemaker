<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.entity.User"%>

<%--
    FINAL CORRECTED SECURITY CHECK:
    This block checks the session directly WITHOUT declaring a duplicate 'user' variable.
--%>
<%
if (session.getAttribute("loggedInUser") == null) {
	session.setAttribute("errorMsg", "Please login to access the dashboard.");
	response.sendRedirect("login.jsp");
	return;
}
%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>Dashboard - AI Resume Maker</title>
<%@include file="all_components/head.jsp"%>
<style>
/* No longer hiding the default cursor */
.feature-card-link {
	text-decoration: none;
	color: inherit;
	display: block;
	height: 100%;
	position: relative;
	will-change: transform;
	transition: transform 0.3s ease-out;
}

.feature-card-link .content-card {
	height: 100%;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	position: relative;
	overflow: hidden;
}

.feature-card-link .content-card::before {
	content: '';
	position: absolute;
	top: 0;
	left: 0;
	width: 200%;
	height: 200%;
	background: conic-gradient(from 180deg at 50% 50%, var(--primary-glow),
		var(--secondary-glow), var(--primary-glow));
	z-index: -1;
	opacity: 0;
	transition: opacity 0.4s ease;
	animation: rotate-glow 5s linear infinite;
}

.feature-card-link:hover .content-card::before {
	opacity: 0.5; /* More subtle hover */
}

/* CORRECTED SYNTAX: @ and keyframes must be on the same line */
@keyframes rotate-glow {
    from { transform: translate(-25%, -25%) rotate(0deg); }
    to { transform: translate(-25%, -25%) rotate(360deg); }
}

.feature-icon {
	font-size: 3rem;
	color: var(--primary-glow);
	text-shadow: 0 0 15px var(--primary-glow);
}

.feature-card-link-container {
	opacity: 0;
	transform: translateY(40px);
	animation: slideInUp 0.6s ease-out forwards;
}

.feature-card-link-container:nth-child(2) { animation-delay: 0.1s; }
.feature-card-link-container:nth-child(3) { animation-delay: 0.2s; }
.feature-card-link-container:nth-child(4) { animation-delay: 0.3s; }
.feature-card-link-container:nth-child(5) { animation-delay: 0.4s; }
.feature-card-link-container:nth-child(6) { animation-delay: 0.5s; }

/* CORRECTED SYNTAX: @ and keyframes must be on the same line */
@keyframes slideInUp {
    to { 
        opacity: 1;
        transform: translateY(0);
    }
}
</style>
</head>
<body>
	<div class="stars">
    <div class="stars-small"></div>
    <div class="stars-medium"></div>
    <div class="stars-large"></div>
</div>

	<main>
		<%@include file="all_components/navbar.jsp"%>

		<div class="container py-5">
			<div class="row mb-4 text-center">
				<div class="col-12">
					<h1 class="display-5">AI Toolkit</h1>
					<p id="welcome-message" class="text-muted"></p>
				</div>
			</div>

			<div class="row g-4">
				<div class="col-lg-4 col-md-6 feature-card-link-container">
					<a href="createscratch.jsp" class="feature-card-link" data-tilt><div class="content-card">
						<div><i class="fas fa-magic feature-icon mb-3"></i><h5 class="fw-bold">Create From Scratch</h5><p class="text-muted">A step-by-step builder to craft a new, professional resume from the ground up.</p></div><span class="fw-bold text-primary">Start Building &rarr;</span>
					</div></a>
				</div>
				<div class="col-lg-4 col-md-6 feature-card-link-container">
					<a href="airesumetailor.jsp" class="feature-card-link" data-tilt><div class="content-card">
						<div><i class="fas fa-bullseye feature-icon mb-3"></i><h5 class="fw-bold">AI Resume Tailor</h5><p class="text-muted">Paste your resume and a job description to get an AI-optimized version in seconds.</p></div><span class="fw-bold text-primary">Start Tailoring &rarr;</span>
					</div></a>
				</div>
				<div class="col-lg-4 col-md-6 feature-card-link-container">
					<a href="atschecker.jsp" class="feature-card-link" data-tilt><div class="content-card">
						<div><i class="fas fa-search feature-icon mb-3"></i><h5 class="fw-bold">ATS Score & Checker</h5><p class="text-muted">Analyze your resume's compatibility with automated screening software (ATS).</p></div><span class="fw-bold text-primary">Analyze Now &rarr;</span>
					</div></a>
				</div>
				<div class="col-lg-4 col-md-6 feature-card-link-container">
					<a href="coverletter.jsp" class="feature-card-link" data-tilt><div class="content-card">
						<div><i class="fas fa-file-signature feature-icon mb-3"></i><h5 class="fw-bold">AI Cover Letter Writer</h5><p class="text-muted">Generate a compelling cover letter tailored to your resume and a specific job.</p></div><span class="fw-bold text-primary">Write Letter &rarr;</span>
					</div></a>
				</div>
				<div class="col-lg-4 col-md-6 feature-card-link-container">
					<a href="mydocuments.jsp" class="feature-card-link" data-tilt><div class="content-card">
						<div><i class="fas fa-folder-open feature-icon mb-3"></i><h5 class="fw-bold">My Document Hub</h5><p class="text-muted">View, manage, edit, and download all your saved resumes and cover letters.</p></div><span class="fw-bold text-primary">Open Hub &rarr;</span>
					</div></a>
				</div>
				<div class="col-lg-4 col-md-6 feature-card-link-container">
					<a href="#" class="feature-card-link" data-tilt><div class="content-card">
						<div><i class="fas fa-lightbulb feature-icon mb-3"></i><h5 class="fw-bold">Interview Prep</h5><p class="text-muted">Get AI-generated interview questions and tips based on your resume and target job.</p></div><span class="fw-bold text-primary">Start Practicing &rarr;</span>
					</div></a>
				</div>
			</div>
		</div>
	</main>
	<%@include file="all_components/footer.jsp"%>
	<script>
    document.addEventListener('DOMContentLoaded', function() {
        
        // RESTORED: 3D Card Tilt Effect
        const tiltCards = document.querySelectorAll('[data-tilt]');
        tiltCards.forEach(card => {
            const tiltStrength = 15;
            card.addEventListener('mousemove', e => {
                const rect = card.getBoundingClientRect();
                const mouseX = (e.clientX - rect.left - rect.width / 2) / (rect.width / 2);
                const mouseY = (e.clientY - rect.top - rect.height / 2) / (rect.height / 2);
                const rotateX = -mouseY * tiltStrength;
                const rotateY = mouseX * tiltStrength;
                card.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
            });
            card.addEventListener('mouseleave', () => {
                card.style.transform = 'perspective(1000px) rotateX(0deg) rotateY(0deg)';
            });
        });
        
        // Typing Welcome Message
        const welcomeEl = document.getElementById('welcome-message');
        const welcomeText = 'Welcome back, <%=user.getFullName()%>! What would you like to create today?';
        let i = 0;
        function typeWriter() {
            if (welcomeEl && i < welcomeText.length) {
                welcomeEl.innerHTML += welcomeText.charAt(i);
                i++;
                setTimeout(typeWriter, 50);
            }
        }
        typeWriter();
    });
    </script>
</body>
</html>