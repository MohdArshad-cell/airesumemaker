<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.entity.User, com.entity.Resume, com.entity.WorkExperience, com.entity.Education, com.entity.Skill, com.entity.Project, com.entity.Certification, com.entity.Achievement, com.entity.Link"%>
<%@ page import="com.DAO.ResumeDAO, com.DAO.WorkExperienceDAO, com.DAO.EducationDAO, com.DAO.SkillDAO, com.DAO.ProjectDAO, com.DAO.CertificationDAO, com.DAO.AchievementDAO, com.DAO.LinkDAO"%>
<%@ page import="com.Db.DBConnect"%>
<%@ page import="java.util.List"%>

<%-- 1. Security Check and Data Fetching --%>
<%
User user = (User) session.getAttribute("loggedInUser");
if (user == null) {
    session.setAttribute("errorMsg", "Please login to continue.");
    response.sendRedirect("login.jsp");
    return;
}

int resumeId = 0;
try {
    resumeId = Integer.parseInt(request.getParameter("id"));
} catch (NumberFormatException e) {
    session.setAttribute("errorMsg", "Invalid Resume ID.");
    response.sendRedirect("mydocuments.jsp");
    return;
}

// DAOs for all entities
ResumeDAO resumeDAO = new ResumeDAO(DBConnect.getConn());
WorkExperienceDAO expDAO = new WorkExperienceDAO(DBConnect.getConn());
EducationDAO eduDAO = new EducationDAO(DBConnect.getConn());
SkillDAO skillDAO = new SkillDAO(DBConnect.getConn());
ProjectDAO projectDAO = new ProjectDAO(DBConnect.getConn());
CertificationDAO certDAO = new CertificationDAO(DBConnect.getConn());
AchievementDAO achievementDAO = new AchievementDAO(DBConnect.getConn());
LinkDAO linkDAO = new LinkDAO(DBConnect.getConn());


// Fetch all data for this resume
Resume resume = resumeDAO.getResumeById(resumeId, user.getUserId());

// Security check failed or resume not found
if (resume == null) {
    session.setAttribute("errorMsg", "Resume not found or you do not have permission to edit it.");
    response.sendRedirect("mydocuments.jsp");
    return;
}

List<WorkExperience> expList = expDAO.getWorkExperienceByResumeId(resumeId);
List<Education> eduList = eduDAO.getEducationByResumeId(resumeId);
List<Skill> skillList = skillDAO.getSkillsByResumeId(resumeId);
List<Project> projectList = projectDAO.getProjectsByResumeId(resumeId);
List<Certification> certList = certDAO.getCertificationsByResumeId(resumeId);
List<Achievement> achievementList = achievementDAO.getAchievementsByResumeId(resumeId);
List<Link> linkList = linkDAO.getLinksByResumeId(resumeId);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>Edit Resume - <%=resume.getTitle()%></title>
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
.content-card .form-control, 
	.content-card .form-select {
		background-color: rgba(0, 0, 0, 0.3) !important;
		border: 1px solid var(--card-border) !important;
		color: var(--text-light) !important;
		border-radius: 8px !important;
	}
	.content-card .form-control:focus, 
	.content-card .form-select:focus {
		background-color: rgba(0, 0, 0, 0.4) !important;
		color: var(--text-light) !important;
		border-color: var(--primary-glow) !important;
		box-shadow: 0 0 8px var(--primary-glow) !important;
	}
	.content-card .form-control::placeholder {
		color: rgba(255, 255, 255, 0.5);
	}
	.content-card .form-control::-webkit-calendar-picker-indicator {
		filter: invert(1);
	}
</style>
</head>
<body>
    <div class="background-container">
        <div class="aurora aurora-1"></div>
        <div class="aurora aurora-2"></div>
    </div>
    <main>
        <jsp:include page="all_components/navbar.jsp" />
        <div class="container py-5">
            <div class="row">
                <div class="col-md-10 mx-auto">
                    <div class="content-card">
                        <h3 class="text-center mb-4">Editing: <%=resume.getTitle()%></h3>
                        <form action="updateResume" method="post">
                            <input type="hidden" name="resumeId" value="<%= resume.getResumeId() %>">
                            <div class="mb-4">
                               <label for="resumeTitle" class="form-label fw-bold h5">Resume Title</label>
                               <input type="text" class="form-control" id="resumeTitle" name="title" value="<%= resume.getTitle() %>" required>
                           </div>
                            <div class="accordion" id="resumeAccordion">
                                
                                <%-- Contact & Summary (Sections 1 & 2) --%>
                                <div class="accordion-item"><h2 class="accordion-header"><button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#c1">Section 1: Contact Information</button></h2><div id="c1" class="accordion-collapse collapse show" data-bs-parent="#resumeAccordion"><div class="accordion-body"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Full Name</label><input type="text" name="fullName" class="form-control" value="<%= resume.getFullName() != null ? resume.getFullName() : "" %>"></div><div class="col-md-6 mb-3"><label class="form-label">Email</label><input type="email" name="email" class="form-control" value="<%= resume.getEmail() != null ? resume.getEmail() : "" %>"></div><div class="col-md-6 mb-3"><label class="form-label">Phone</label><input type="tel" name="phone" class="form-control" value="<%= resume.getPhone() != null ? resume.getPhone() : "" %>"></div><div class="col-md-6 mb-3"><label class="form-label">Address</label><input type="text" name="address" class="form-control" value="<%= resume.getAddress() != null ? resume.getAddress() : "" %>"></div></div></div></div></div>
                                <div class="accordion-item"><h2 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#c2">Section 2: Professional Summary</button></h2><div id="c2" class="accordion-collapse collapse" data-bs-parent="#resumeAccordion"><div class="accordion-body"><label class="form-label">Summary</label><textarea name="summary" class="form-control" rows="5"><%= resume.getSummary() != null ? resume.getSummary() : "" %></textarea></div></div></div>

                                <%-- Work Experience (Section 3) --%>
                                <div class="accordion-item"><h2 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#c3">Section 3: Work Experience</button></h2><div id="c3" class="accordion-collapse collapse" data-bs-parent="#resumeAccordion"><div class="accordion-body"><div id="experience-container"><% for (WorkExperience exp : expList) { %><div class="dynamic-entry"><h5>Experience Entry</h5><input type="hidden" name="experienceId" value="<%= exp.getExperienceId() %>"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Job Title</label><input type="text" name="jobTitle" class="form-control" value="<%= exp.getJobTitle() != null ? exp.getJobTitle() : "" %>"></div><div class="col-md-6 mb-3"><label class="form-label">Company</label><input type="text" name="company" class="form-control" value="<%= exp.getCompany() != null ? exp.getCompany() : "" %>"></div><div class="col-md-4 mb-3"><label class="form-label">Start Date</label><input type="date" name="startDate" class="form-control" value="<%= exp.getStartDate() != null ? exp.getStartDate() : "" %>"></div><div class="col-md-4 mb-3"><label class="form-label">End Date</label><input type="date" name="endDate" class="form-control" value="<%= exp.getEndDate() != null ? exp.getEndDate() : "" %>"></div><div class="col-md-4 mb-3"><label class="form-label">Location</label><input type="text" name="location" class="form-control" value="<%= exp.getLocation() != null ? exp.getLocation() : "" %>"></div><div class="col-12"><label class="form-label">Description</label><textarea name="exp_description" class="form-control" rows="4"><%= exp.getDescription() != null ? exp.getDescription() : "" %></textarea></div></div></div><% } %></div><button type="button" id="add-experience-btn" class="btn btn-secondary mt-2"><i class="fas fa-plus me-2"></i>Add Experience</button></div></div></div>

                                <%-- Education (Section 4) --%>
                                <div class="accordion-item"><h2 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#c4">Section 4: Education</button></h2><div id="c4" class="accordion-collapse collapse" data-bs-parent="#resumeAccordion"><div class="accordion-body"><div id="education-container"><% for (Education edu : eduList) { %><div class="dynamic-entry"><h5>Education Entry</h5><input type="hidden" name="educationId" value="<%= edu.getEducationId() %>"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Institution</label><input type="text" name="institution" class="form-control" value="<%= edu.getInstitution() != null ? edu.getInstitution() : "" %>"></div><div class="col-md-6 mb-3"><label class="form-label">Degree</label><input type="text" name="degree" class="form-control" value="<%= edu.getDegree() != null ? edu.getDegree() : "" %>"></div><div class="col-md-6 mb-3"><label class="form-label">Field of Study</label><input type="text" name="fieldOfStudy" class="form-control" value="<%= edu.getFieldOfStudy() != null ? edu.getFieldOfStudy() : "" %>"></div><div class="col-md-6 mb-3"><label class="form-label">Graduation Year</label><input type="number" name="gradYear" class="form-control" value="<%= edu.getGraduationYear() != 0 ? edu.getGraduationYear() : "" %>"></div></div></div><% } %></div><button type="button" id="add-education-btn" class="btn btn-secondary mt-2"><i class="fas fa-plus me-2"></i>Add Education</button></div></div></div>

                                <%-- Skills (Section 5) --%>
                                <div class="accordion-item"><h2 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#c5">Section 5: Skills</button></h2><div id="c5" class="accordion-collapse collapse" data-bs-parent="#resumeAccordion"><div class="accordion-body"><div id="skills-container"><% for (Skill skill : skillList) { %><div class="dynamic-entry"><h5>Skill Entry</h5><input type="hidden" name="skillId" value="<%= skill.getSkillId() %>"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Skill Name</label><input type="text" name="skillName" class="form-control" value="<%= skill.getSkillName() != null ? skill.getSkillName() : "" %>"></div><div class="col-md-6 mb-3"><label class="form-label">Proficiency</label><select name="skillLevel" class="form-control"><option <%="Beginner".equals(skill.getSkillLevel()) ? "selected" : ""%>>Beginner</option><option <%="Intermediate".equals(skill.getSkillLevel()) ? "selected" : ""%>>Intermediate</option><option <%="Advanced".equals(skill.getSkillLevel()) ? "selected" : ""%>>Advanced</option><option <%="Expert".equals(skill.getSkillLevel()) ? "selected" : ""%>>Expert</option></select></div></div></div><% } %></div><button type="button" id="add-skill-btn" class="btn btn-secondary mt-2"><i class="fas fa-plus me-2"></i>Add Skill</button></div></div></div>

                                <%-- NEW: Projects (Section 6) --%>
                                <div class="accordion-item"><h2 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#c6">Section 6: Projects</button></h2><div id="c6" class="accordion-collapse collapse" data-bs-parent="#resumeAccordion"><div class="accordion-body"><div id="projects-container"><% for (Project proj : projectList) { %><div class="dynamic-entry"><h5>Project Entry</h5><input type="hidden" name="projectId" value="<%= proj.getProjectId() %>"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Project Title</label><input type="text" name="projectTitle" class="form-control" value="<%= proj.getProjectTitle() != null ? proj.getProjectTitle() : "" %>"></div><div class="col-md-6 mb-3"><label class="form-label">Project Link (Optional)</label><input type="url" name="projectLink" class="form-control" value="<%= proj.getProjectLink() != null ? proj.getProjectLink() : "" %>"></div><div class="col-12"><label class="form-label">Description</label><textarea name="proj_description" class="form-control" rows="3"><%= proj.getDescription() != null ? proj.getDescription() : "" %></textarea></div></div></div><% } %></div><button type="button" id="add-project-btn" class="btn btn-secondary mt-2"><i class="fas fa-plus me-2"></i>Add Project</button></div></div></div>

                                <%-- NEW: Certifications (Section 7) --%>
                                <div class="accordion-item"><h2 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#c7">Section 7: Certifications</button></h2><div id="c7" class="accordion-collapse collapse" data-bs-parent="#resumeAccordion"><div class="accordion-body"><div id="certs-container"><% for (Certification cert : certList) { %><div class="dynamic-entry"><h5>Certification Entry</h5><input type="hidden" name="certId" value="<%= cert.getCertId() %>"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Certification Name</label><input type="text" name="certName" class="form-control" value="<%= cert.getCertName() != null ? cert.getCertName() : "" %>"></div><div class="col-md-6 mb-3"><label class="form-label">Issuing Organization</label><input type="text" name="certOrg" class="form-control" value="<%= cert.getCertOrg() != null ? cert.getCertOrg() : "" %>"></div></div></div><% } %></div><button type="button" id="add-cert-btn" class="btn btn-secondary mt-2"><i class="fas fa-plus me-2"></i>Add Certification</button></div></div></div>
                                
                                <%-- NEW: Achievements (Section 8) --%>
                                <div class="accordion-item"><h2 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#c8">Section 8: Awards & Achievements</button></h2><div id="c8" class="accordion-collapse collapse" data-bs-parent="#resumeAccordion"><div class="accordion-body"><div id="achievements-container"><% for (Achievement ach : achievementList) { %><div class="dynamic-entry"><h5>Achievement Entry</h5><input type="hidden" name="achievementId" value="<%= ach.getAchievementId() %>"><div class="row"><div class="col-12"><label class="form-label">Description</label><input type="text" name="achievementDesc" class="form-control" value="<%= ach.getDescription() != null ? ach.getDescription() : "" %>"></div></div></div><% } %></div><button type="button" id="add-achievement-btn" class="btn btn-secondary mt-2"><i class="fas fa-plus me-2"></i>Add Achievement</button></div></div></div>
                                
                                <%-- NEW: Links (Section 9) --%>
                                <div class="accordion-item"><h2 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#c9">Section 9: Web & Social Links</button></h2><div id="c9" class="accordion-collapse collapse" data-bs-parent="#resumeAccordion"><div class="accordion-body"><div id="links-container"><% for (Link link : linkList) { %><div class="dynamic-entry"><h5>Link Entry</h5><input type="hidden" name="linkId" value="<%= link.getLinkId() %>"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Link Name (e.g., LinkedIn, GitHub)</label><input type="text" name="linkName" class="form-control" value="<%= link.getLinkName() != null ? link.getLinkName() : "" %>"></div><div class="col-md-6 mb-3"><label class="form-label">URL</label><input type="url" name="linkUrl" class="form-control" value="<%= link.getLinkUrl() != null ? link.getLinkUrl() : "" %>"></div></div></div><% } %></div><button type="button" id="add-link-btn" class="btn btn-secondary mt-2"><i class="fas fa-plus me-2"></i>Add Link</button></div></div></div>

                            </div>
                            <div class="d-grid mt-4">
                                <button type="submit" class="btn btn-futuristic btn-lg">Update Resume</button>
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
        // Generic function to add a new dynamic block
        function addBlock(containerId, htmlContent) {
            const container = document.getElementById(containerId);
            const div = document.createElement('div');
            div.classList.add('dynamic-entry');
            div.innerHTML = htmlContent;
            container.appendChild(div);
        }

        // --- Add Experience ---
        document.getElementById('add-experience-btn').addEventListener('click', () => {
            const html = `<h5>New Experience Entry</h5><input type="hidden" name="experienceId" value="0"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Job Title</label><input type="text" name="jobTitle" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Company</label><input type="text" name="company" class="form-control"></div><div class="col-md-4 mb-3"><label class="form-label">Start Date</label><input type="date" name="startDate" class="form-control"></div><div class="col-md-4 mb-3"><label class="form-label">End Date</label><input type="date" name="endDate" class="form-control"></div><div class="col-md-4 mb-3"><label class="form-label">Location</label><input type="text" name="location" class="form-control"></div><div class="col-12"><label class="form-label">Description</label><textarea name="exp_description" class="form-control" rows="4"></textarea></div></div>`;
            addBlock('experience-container', html);
        });

        // --- Add Education ---
        document.getElementById('add-education-btn').addEventListener('click', () => {
            const html = `<h5>New Education Entry</h5><input type="hidden" name="educationId" value="0"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Institution</label><input type="text" name="institution" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Degree</label><input type="text" name="degree" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Field of Study</label><input type="text" name="fieldOfStudy" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Graduation Year</label><input type="number" name="gradYear" class="form-control"></div></div>`;
            addBlock('education-container', html);
        });

        // --- Add Skill ---
        document.getElementById('add-skill-btn').addEventListener('click', () => {
            const html = `<h5>New Skill Entry</h5><input type="hidden" name="skillId" value="0"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Skill Name</label><input type="text" name="skillName" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Proficiency</label><select name="skillLevel" class="form-control"><option>Beginner</option><option>Intermediate</option><option>Advanced</option><option>Expert</option></select></div></div>`;
            addBlock('skills-container', html);
        });

        // --- Add Project ---
        document.getElementById('add-project-btn').addEventListener('click', () => {
            const html = `<h5>New Project Entry</h5><input type="hidden" name="projectId" value="0"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Project Title</label><input type="text" name="projectTitle" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Project Link (Optional)</label><input type="url" name="projectLink" class="form-control"></div><div class="col-12"><label class="form-label">Description</label><textarea name="proj_description" class="form-control" rows="3"></textarea></div></div>`;
            addBlock('projects-container', html);
        });

        // --- Add Certification ---
        document.getElementById('add-cert-btn').addEventListener('click', () => {
            const html = `<h5>New Certification Entry</h5><input type="hidden" name="certId" value="0"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Certification Name</label><input type="text" name="certName" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">Issuing Organization</label><input type="text" name="certOrg" class="form-control"></div></div>`;
            addBlock('certs-container', html);
        });

        // --- Add Achievement ---
        document.getElementById('add-achievement-btn').addEventListener('click', () => {
            const html = `<h5>New Achievement Entry</h5><input type="hidden" name="achievementId" value="0"><div class="row"><div class="col-12"><label class="form-label">Description</label><input type="text" name="achievementDesc" class="form-control"></div></div>`;
            addBlock('achievements-container', html);
        });

        // --- Add Link ---
        document.getElementById('add-link-btn').addEventListener('click', () => {
            const html = `<h5>New Link Entry</h5><input type="hidden" name="linkId" value="0"><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Link Name (e.g., LinkedIn, GitHub)</label><input type="text" name="linkName" class="form-control"></div><div class="col-md-6 mb-3"><label class="form-label">URL</label><input type="url" name="linkUrl" class="form-control"></div></div>`;
            addBlock('links-container', html);
        });
    });
    </script>
</body>
</html>