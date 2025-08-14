<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.entity.User"%>

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
<title>Create Resume From Scratch - AI Resume Maker</title>
<%@include file="all_components/head.jsp"%>
<style>
.accordion-item {
	background: var(--card-bg);
	border: 1px solid var(--card-border);
	margin-bottom: 1rem;
	border-radius: 16px !important;
}

.accordion-button {
	background: rgba(0, 0, 0, 0.2);
	color: var(--text-light);
	font-weight: 700;
}

.accordion-button:not(.collapsed) {
	background: var(--card-bg);
	color: var(--primary-glow);
	box-shadow: none;
}

.accordion-button:focus {
	box-shadow: 0 0 10px var(--primary-glow);
}

.dynamic-entry {
	border: 1px solid var(--card-border);
	padding: 1rem;
	border-radius: 8px;
	margin-bottom: 1rem;
	background: rgba(0, 0, 0, 0.2);
}
/* FIX: Make text in dynamic sections visible */
/* FIX: Make all form text inside the accordion visible */
.accordion-body .form-label, .dynamic-entry h5 {
    color: var(--text-light);
    opacity: 0.9; /* Makes it slightly less strong than main headings */
}

</style>
</head>
<body>
	<div class="background-container">
		<div class="aurora aurora-1"></div>
		<div class="aurora aurora-2"></div>
	</div>

	<main>
		<%@include file="all_components/navbar.jsp"%>

		<div class="container py-5">
			<div class="row">
				<div class="col-md-10 mx-auto">
					<div class="content-card">
						<h3 class="text-center mb-4">Build Your Resume From Scratch</h3>
						<form action="saveResume" method="post">
						 <div class="mb-4">
        <label for="resumeTitle" class="form-label fw-bold h5">Resume Title</label>
        <input type="text" class="form-control" id="resumeTitle" name="title" placeholder="e.g., Senior Java Developer Resume" required>
    </div>
							<div class="accordion" id="resumeAccordion">

								<div class="accordion-item">
									<h2 class="accordion-header">
										<button class="accordion-button" type="button"
											data-bs-toggle="collapse" data-bs-target="#collapseContact">Section
											1: Contact Information</button>
									</h2>
									<div id="collapseContact"
										class="accordion-collapse collapse show"
										data-bs-parent="#resumeAccordion">
										<div class="accordion-body">
											<div class="row">
												<div class="col-md-6 mb-3">
													<label class="form-label">Full Name</label><input
														type="text" name="fullName" class="form-control">
												</div>
												<div class="col-md-6 mb-3">
													<label class="form-label">Email Address</label><input
														type="email" name="email" class="form-control">
												</div>
												<div class="col-md-6 mb-3">
													<label class="form-label">Phone Number</label><input
														type="tel" name="phone" class="form-control">
												</div>
												<div class="col-md-6 mb-3">
													<label class="form-label">Address / Location</label><input
														type="text" name="address" class="form-control">
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="accordion-item">
									<h2 class="accordion-header">
										<button class="accordion-button collapsed" type="button"
											data-bs-toggle="collapse" data-bs-target="#collapseSummary">Section
											2: Professional Summary</button>
									</h2>
									<div id="collapseSummary" class="accordion-collapse collapse"
										data-bs-parent="#resumeAccordion">
										<div class="accordion-body">
											<label class="form-label">Summary</label>
											<textarea name="summary" class="form-control" rows="5"></textarea>
										</div>
									</div>
								</div>

								<div class="accordion-item">
									<h2 class="accordion-header">
										<button class="accordion-button collapsed" type="button"
											data-bs-toggle="collapse"
											data-bs-target="#collapseExperience">Section 3: Work
											Experience</button>
									</h2>
									<div id="collapseExperience"
										class="accordion-collapse collapse"
										data-bs-parent="#resumeAccordion">
										<div class="accordion-body">
											<div id="experience-container"></div>
											<button type="button" id="add-experience-btn"
												class="btn btn-secondary mt-2">
												<i class="fas fa-plus me-2"></i>Add Experience
											</button>
										</div>
									</div>
								</div>

								<div class="accordion-item">
									<h2 class="accordion-header">
										<button class="accordion-button collapsed" type="button"
											data-bs-toggle="collapse" data-bs-target="#collapseEducation">Section
											4: Education</button>
									</h2>
									<div id="collapseEducation" class="accordion-collapse collapse"
										data-bs-parent="#resumeAccordion">
										<div class="accordion-body">
											<div id="education-container"></div>
											<button type="button" id="add-education-btn"
												class="btn btn-secondary mt-2">
												<i class="fas fa-plus me-2"></i>Add Education
											</button>
										</div>
									</div>
								</div>

								<div class="accordion-item">
									<h2 class="accordion-header">
										<button class="accordion-button collapsed" type="button"
											data-bs-toggle="collapse" data-bs-target="#collapseSkills">Section
											5: Skills</button>
									</h2>
									<div id="collapseSkills" class="accordion-collapse collapse"
										data-bs-parent="#resumeAccordion">
										<div class="accordion-body">
											<div id="skills-container"></div>
											<button type="button" id="add-skill-btn"
												class="btn btn-secondary mt-2">
												<i class="fas fa-plus me-2"></i>Add Skill
											</button>
										</div>
									</div>
								</div>

								<div class="accordion-item">
									<h2 class="accordion-header">
										<button class="accordion-button collapsed" type="button"
											data-bs-toggle="collapse" data-bs-target="#collapseProjects">Section
											6: Projects</button>
									</h2>
									<div id="collapseProjects" class="accordion-collapse collapse"
										data-bs-parent="#resumeAccordion">
										<div class="accordion-body">
											<div id="projects-container"></div>
											<button type="button" id="add-project-btn"
												class="btn btn-secondary mt-2">
												<i class="fas fa-plus me-2"></i>Add Project
											</button>
										</div>
									</div>
								</div>

								<div class="accordion-item">
									<h2 class="accordion-header">
										<button class="accordion-button collapsed" type="button"
											data-bs-toggle="collapse" data-bs-target="#collapseCerts">Section
											7: Certifications</button>
									</h2>
									<div id="collapseCerts" class="accordion-collapse collapse"
										data-bs-parent="#resumeAccordion">
										<div class="accordion-body">
											<div id="certs-container"></div>
											<button type="button" id="add-cert-btn"
												class="btn btn-secondary mt-2">
												<i class="fas fa-plus me-2"></i>Add Certification
											</button>
										</div>
									</div>
								</div>

								<div class="accordion-item">
									<h2 class="accordion-header">
										<button class="accordion-button collapsed" type="button"
											data-bs-toggle="collapse"
											data-bs-target="#collapseAchievements">Section 8:
											Awards & Achievements</button>
									</h2>
									<div id="collapseAchievements"
										class="accordion-collapse collapse"
										data-bs-parent="#resumeAccordion">
										<div class="accordion-body">
											<div id="achievements-container"></div>
											<button type="button" id="add-achievement-btn"
												class="btn btn-secondary mt-2">
												<i class="fas fa-plus me-2"></i>Add Achievement
											</button>
										</div>
									</div>
								</div>

								<div class="accordion-item">
									<h2 class="accordion-header">
										<button class="accordion-button collapsed" type="button"
											data-bs-toggle="collapse" data-bs-target="#collapseLinks">Section
											9: Web & Social Links</button>
									</h2>
									<div id="collapseLinks" class="accordion-collapse collapse"
										data-bs-parent="#resumeAccordion">
										<div class="accordion-body">
											<div id="links-container"></div>
											<button type="button" id="add-link-btn"
												class="btn btn-secondary mt-2">
												<i class="fas fa-plus me-2"></i>Add Link
											</button>
										</div>
									</div>
								</div>
							</div>

							<div class="d-grid mt-4">
								<button type="submit" class="btn btn-futuristic btn-lg">Save
									Resume</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</main>
	<%@include file="all_components/footer.jsp"%>
	<script>
    document.addEventListener('DOMContentLoaded', function() {
        // --- Generic function to add a new block ---
        function addBlock(containerId, htmlContent) {
            const container = document.getElementById(containerId);
            const div = document.createElement('div');
            div.classList.add('dynamic-entry');
            
            // The count is now just for the visual heading, not the name attribute
            const count = container.children.length + 1;
            div.innerHTML = htmlContent.replace(/#COUNT#/g, count); 
            container.appendChild(div);
        }

        // --- Work Experience (Corrected names) ---
        const expContainer = 'experience-container';
        const expBtn = document.getElementById('add-experience-btn');
        const expHtml = `<h5>Experience ##COUNT#</h5><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Job Title</label><input type="text" name="jobTitle" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Company</label><input type="text" name="company" class="form-control"></div><div class="col-md-4 mb-3"><label class="form-label">Start Date</label><input type="date" name="startDate" class="form-control"></div><div class="col-md-4 mb-3"><label class="form-label">End Date</label><input type="date" name="endDate" class="form-control"></div><div class="col-md-4 mb-3"><label class="form-label">Location</label><input type="text" name="location" class="form-control"></div><div class="col-12"><label class="form-label">Description</label><textarea name="exp_description" class="form-control" rows="4"></textarea></div></div>`;
        expBtn.addEventListener('click', () => addBlock(expContainer, expHtml));
        addBlock(expContainer, expHtml); // Add first block automatically

        // --- Education (Corrected names) ---
        const eduContainer = 'education-container';
        const eduBtn = document.getElementById('add-education-btn');
        const eduHtml = `<h5>Education ##COUNT#</h5><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Institution</label><input type="text" name="institution" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Degree</label><input type="text" name="degree" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Field of Study</label><input type="text" name="fieldOfStudy" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Graduation Year</label><input type="number" name="gradYear" class="form-control"></div></div>`;
        eduBtn.addEventListener('click', () => addBlock(eduContainer, eduHtml));
        addBlock(eduContainer, eduHtml);

        // --- Skills (Corrected names) ---
        const skillsContainer = 'skills-container';
        const skillBtn = document.getElementById('add-skill-btn');
        const skillHtml = `<h5>Skill ##COUNT#</h5><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Skill Name</label><input type="text" name="skillName" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Proficiency</label><select name="skillLevel" class="form-control"><option>Beginner</option><option>Intermediate</option><option>Advanced</option><option>Expert</option></select></div></div>`;
        skillBtn.addEventListener('click', () => addBlock(skillsContainer, skillHtml));
        addBlock(skillsContainer, skillHtml);
        
        // --- Projects (Corrected names) ---
        const projectsContainer = 'projects-container';
        const projectBtn = document.getElementById('add-project-btn');
        const projectHtml = `<h5>Project ##COUNT#</h5><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Project Title</label><input type="text" name="projectTitle" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Project Link (Optional)</label><input type="url" name="projectLink" class="form-control"></div><div class="col-12"><label class="form-label">Description</label><textarea name="proj_description" class="form-control" rows="3"></textarea></div></div>`;
        projectBtn.addEventListener('click', () => addBlock(projectsContainer, projectHtml));
        addBlock(projectsContainer, projectHtml);
        
        // --- Certifications (Corrected names) ---
        const certsContainer = 'certs-container';
        const certBtn = document.getElementById('add-cert-btn');
        const certHtml = `<h5>Certification ##COUNT#</h5><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Certification Name</label><input type="text" name="certName" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Issuing Organization</label><input type="text" name="certOrg" class="form-control"></div></div>`;
        certBtn.addEventListener('click', () => addBlock(certsContainer, certHtml));
        addBlock(certsContainer, certHtml);

        // --- Achievements (Corrected names) ---
        const achievementsContainer = 'achievements-container';
        const achievementBtn = document.getElementById('add-achievement-btn');
        const achievementHtml = `<h5>Award / Achievement ##COUNT#</h5><div class="row"><div class="col-12"><label class="form-label">Description</label><input type="text" name="achievementDesc" class="form-control"></div></div>`;
        achievementBtn.addEventListener('click', () => addBlock(achievementsContainer, achievementHtml));
        addBlock(achievementsContainer, achievementHtml);
        
        // --- Links (Corrected names) ---
        const linksContainer = 'links-container';
        const linkBtn = document.getElementById('add-link-btn');
        const linkHtml = `<h5>Link ##COUNT#</h5><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Link Name (e.g., LinkedIn, GitHub)</label><input type="text" name="linkName" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">URL</label><input type="url" name="linkUrl" class="form-control"></div></div>`;
        linkBtn.addEventListener('click', () => addBlock(linksContainer, linkHtml));
        addBlock(linksContainer, linkHtml);
    });
</script>
</body>
</html>