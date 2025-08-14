<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.entity.User"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>AI Resume Maker | Engineer Your Future</title>
<%@include file="all_components/head.jsp"%>
<style>
/* --- Page-Specific Styles for index.jsp --- */

/* Hero Section & Animations */
.hero-section {
	min-height: 90vh;
	display: flex;
	align-items: center;
	justify-content: center;
	text-align: center;
	position: relative;
	perspective: 1000px;
}

.hero-content {
	transition: transform 0.5s cubic-bezier(0.2, 0.8, 0.2, 1);
	will-change: transform;
}

.hero-headline {
	font-weight: 900;
	font-size: clamp(2.5rem, 8vw, 5rem);
	color: var(--text-light);
	text-shadow: 0 0 15px var(--primary-glow), 0 0 25px
		var(--secondary-glow);
	letter-spacing: -2px;
}

.hero-headline-line {
	overflow: hidden;
}

.hero-headline-line span {
	display: block;
	transform: translateY(110%);
	transition: transform 1s cubic-bezier(0.2, 0.8, 0.2, 1);
}

.hero-section.visible .hero-headline-line span {
	transform: translateY(0);
}

.hero-section.visible .hero-headline-line:nth-child(2) span {
	transition-delay: 0.1s;
}

/* Features Section */
.features-section {
	padding: 6rem 0;
}

.feature-icon {
	font-size: 3rem;
	margin-bottom: 1.5rem;
	color: var(--primary-glow);
}

.section-title {
	font-weight: 700;
	color: var(--text-light);
}

/* General Reveal Animation */
.reveal {
	opacity: 0;
	transform: translateY(30px);
	transition: opacity 1s ease-out, transform 1s ease-out;
}

.reveal.visible {
	opacity: 1;
	transform: translateY(0);
}
</style>
</head>
<body>
	<div class="background-container">
		<div class="aurora aurora-1"></div>
		<div class="aurora aurora-2"></div>
	</div>

	<main>
		<%-- FINAL PATTERN: Declare 'user' once in the main page before including the simple navbar --%>

		<%@include file="all_components/navbar.jsp"%>

		<section
			class="hero-section d-flex align-items-center justify-content-center text-center reveal"
			style="min-height: 90vh;">
			<div class="container hero-content">
				<div class="hero-headline">
					<div class="hero-headline-line">
						<span>Don't Just Apply.</span>
					</div>
					<div class="hero-headline-line">
						<span>Get an Interview.</span>
					</div>
				</div>
				<p class="lead text-muted my-4"
					style="max-width: 650px; margin-left: auto; margin-right: auto;">
					Our intelligent platform helps you beat the screening bots and
					create a professional resume that captivates hiring managers.</p>
				<div>
					<div>
    <% if (user != null) { %>
        <%-- If user IS logged in, the button links to their dashboard --%>
        <a class="btn btn-futuristic" href="home.jsp" role="button">Go To Dashboard</a>
    <% } else { %>
        <%-- If user is NOT logged in, the button links to the login page --%>
        <a class="btn btn-futuristic" href="login.jsp" role="button">Create My Resume Now</a>
    <% } %>
</div>
				</div>
			</div>
		</section>

		<section class="features-section py-5">

			<div class="container">

				<div class="row text-center mb-5 reveal">

					<div class="col-12">

						<h2 class="section-title">An Entire Suite of AI-Powered Tools</h2>

						<p class="text-muted">From your first draft to your final
							interview, we've got you covered.</p>

					</div>

				</div>



				<div class="feature-filter-nav reveal">

					<button class="filter-btn active" data-filter="core">Core
						Features</button>

					<button class="filter-btn" data-filter="advanced">Advanced
						Tools</button>

				</div>



				<div class="row g-4">


					<div class="col-lg-4 col-md-6 feature-card-container"
						data-category="core">

						<div class="content-card h-100 d-flex flex-column">

							<div class="flex-grow-1">

								<i class="fas fa-magic fa-3x mb-3"
									style="color: var(--primary-glow);"></i>

								<h5 class="fw-bold">AI Writing Assistant</h5>

								<p class="text-muted">Our AI rewrites your experience using
									powerful action verbs and industry-specific keywords to make
									your accomplishments shine.</p>

							</div>

							<p class="mt-3 fw-bold mb-0"
								style="color: var(--secondary-glow);">
								<i class="fas fa-check-circle me-2"></i>How it helps: You sound
								more professional and confident, instantly grabbing a
								recruiter's attention.
							</p>

						</div>

					</div>

					<div class="col-lg-4 col-md-6 feature-card-container"
						data-category="core">

						<div class="content-card h-100 d-flex flex-column">

							<div class="flex-grow-1">

								<i class="fas fa-search fa-3x mb-3"
									style="color: var(--primary-glow);"></i>

								<h5 class="fw-bold">Job Description Matcher</h5>

								<p class="text-muted">Paste any job description and our AI
									will instantly analyze it, highlighting the critical skills and
									keywords you need to include.</p>

							</div>

							<p class="mt-3 fw-bold mb-0"
								style="color: var(--secondary-glow);">
								<i class="fas fa-check-circle me-2"></i>How it helps: Tailor
								your resume for any job in seconds, dramatically increasing your
								chances of beating ATS bots.
							</p>

						</div>

					</div>

					<div class="col-lg-4 col-md-6 feature-card-container"
						data-category="core">

						<div class="content-card h-100 d-flex flex-column">

							<div class="flex-grow-1">

								<i class="fas fa-file-alt fa-3x mb-3"
									style="color: var(--primary-glow);"></i>

								<h5 class="fw-bold">Recruiter-Approved Designs</h5>

								<p class="text-muted">Choose from a library of modern,
									professional templates that are proven to be friendly with
									Applicant Tracking Systems (ATS).</p>

							</div>

							<p class="mt-3 fw-bold mb-0"
								style="color: var(--secondary-glow);">
								<i class="fas fa-check-circle me-2"></i>How it helps: Your
								resume is guaranteed to be easy to read for both screening
								software and human hiring managers.
							</p>

						</div>

					</div>



					<div class="col-lg-4 col-md-6 feature-card-container d-none"
						data-category="advanced">

						<div class="content-card h-100 d-flex flex-column">

							<div class="flex-grow-1">

								<i class="fab fa-linkedin fa-3x mb-3"
									style="color: var(--primary-glow);"></i>

								<h5 class="fw-bold">LinkedIn Profile Sync</h5>

								<p class="text-muted">Import your work history and education
									directly from your LinkedIn profile to get a head start on your
									resume.</p>

							</div>

							<p class="mt-3 fw-bold mb-0"
								style="color: var(--secondary-glow);">
								<i class="fas fa-check-circle me-2"></i>How it helps: Save time
								and ensure consistency between your professional profile and
								your resume.
							</p>

						</div>

					</div>

					<div class="col-lg-4 col-md-6 feature-card-container d-none"
						data-category="advanced">

						<div class="content-card h-100 d-flex flex-column">

							<div class="flex-grow-1">

								<i class="fas fa-file-signature fa-3x mb-3"
									style="color: var(--primary-glow);"></i>

								<h5 class="fw-bold">AI Cover Letter Writer</h5>

								<p class="text-muted">Generate a compelling cover letter
									that is perfectly tailored to your new resume and target job
									description.</p>

							</div>

							<p class="mt-3 fw-bold mb-0"
								style="color: var(--secondary-glow);">
								<i class="fas fa-check-circle me-2"></i>How it helps: Create a
								professional cover letter in a fraction of the time, making your
								application stand out.
							</p>

						</div>

					</div>

					<div class="col-lg-4 col-md-6 feature-card-container d-none"
						data-category="advanced">

						<div class="content-card h-100 d-flex flex-column">

							<div class="flex-grow-1">

								<i class="fas fa-lightbulb fa-3x mb-3"
									style="color: var(--primary-glow);"></i>

								<h5 class="fw-bold">Interview Prep</h5>

								<p class="text-muted">Our AI generates potential interview
									questions and talking points based on your resume and the job
									you're applying for.</p>

							</div>

							<p class="mt-3 fw-bold mb-0"
								style="color: var(--secondary-glow);">
								<i class="fas fa-check-circle me-2"></i>How it helps: Walk into
								your interview with confidence, fully prepared for any question.
							</p>

						</div>

					</div>

				</div>

			</div>

		</section>

		<%@include file="all_components/footer.jsp"%>
	</main>

	<script>
document.addEventListener('DOMContentLoaded', function() {

    // --- 1. On-Scroll & On-Load Reveal Animation ---
    const revealElements = document.querySelectorAll('.reveal');
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            }
        });
    }, { threshold: 0.1 });
    revealElements.forEach(el => observer.observe(el));

    
    // --- 2. Hero Section Parallax Mouse Effect ---
    const heroSection = document.querySelector('.hero-section');
    const heroContent = document.querySelector('.hero-content');
    if (heroSection && heroContent) {
        heroSection.addEventListener('mousemove', (e) => {
            const { clientX, clientY } = e;
            const { offsetWidth, offsetHeight } = heroSection;
            
            const xRotation = ((clientY - offsetHeight / 2) / (offsetHeight / 2)) * -8; // Rotation strength
            const yRotation = ((clientX - offsetWidth / 2) / (offsetWidth / 2)) * 8;
            
            heroContent.style.transform = `rotateX(${xRotation}deg) rotateY(${yRotation}deg)`;
        });

        heroSection.addEventListener('mouseleave', () => {
            heroContent.style.transform = `rotateX(0deg) rotateY(0deg)`;
        });
    }

    
    // --- 3. Interactive Feature Filter with Animation ---
    const filterBtns = document.querySelectorAll('.filter-btn');
    const featureCards = document.querySelectorAll('.feature-card-container');

    function filterFeatures(filterValue) {
        featureCards.forEach(card => {
            const isVisible = card.dataset.category === filterValue;
            
            if (!isVisible) {
                // Animate out
                card.style.opacity = '0';
                card.style.transform = 'scale(0.95)';
                setTimeout(() => { card.classList.add('d-none'); }, 400); // Wait for animation
            } else {
                // Animate in
                card.classList.remove('d-none');
                setTimeout(() => {
                    card.style.opacity = '1';
                    card.style.transform = 'scale(1)';
                }, 10); // Tiny delay for display change
            }
        });
    }

    filterBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            // Update active button style
            filterBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            
            const filter = btn.dataset.filter;
            filterFeatures(filter);
        });
    });

    // Set the initial state on page load
    const initialFilter = document.querySelector('.filter-btn.active')?.dataset.filter;
    if(initialFilter){
        filterFeatures(initialFilter);
    }

    
    // --- 4. Interactive Hover Glow for Cards ---
    const interactiveCards = document.querySelectorAll('.content-card');
    interactiveCards.forEach(card => {
        card.addEventListener('mousemove', e => {
            const rect = card.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            
            card.style.setProperty('--mouse-x', `${x}px`);
            card.style.setProperty('--mouse-y', `${y}px`);
        });
    });
});
</script>
</body>
</html>