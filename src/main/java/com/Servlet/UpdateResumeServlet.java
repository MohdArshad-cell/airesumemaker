package com.Servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import com.DAO.*;
import com.Db.DBConnect;
import com.entity.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/updateResume")
public class UpdateResumeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            session.setAttribute("errorMsg", "You must be logged in to update a resume.");
            resp.sendRedirect("login.jsp");
            return;
        }

        int resumeId = -1;
        Connection conn = null;

        try {
            resumeId = Integer.parseInt(req.getParameter("resumeId"));
            conn = DBConnect.getConn();
            conn.setAutoCommit(false); // Start transaction

            // === 1. Update the Main Resume Details ===
            ResumeDAO resumeDAO = new ResumeDAO(conn);
            Resume resume = new Resume();
            resume.setResumeId(resumeId);
            resume.setTitle(req.getParameter("title"));
            resume.setFullName(req.getParameter("fullName"));
            resume.setEmail(req.getParameter("email"));
            resume.setPhone(req.getParameter("phone"));
            resume.setAddress(req.getParameter("address"));
            resume.setSummary(req.getParameter("summary"));
            resumeDAO.updateResume(resume);

            // === 2. Update Work Experience (Delete and Re-insert) ===
            WorkExperienceDAO expDAO = new WorkExperienceDAO(conn);
            expDAO.deleteWorkExperienceByResumeId(resumeId);
            String[] jobTitles = req.getParameterValues("jobTitle");
            if (jobTitles != null) {
                for (int i = 0; i < jobTitles.length; i++) {
                    if (jobTitles[i] == null || jobTitles[i].trim().isEmpty()) continue;
                    WorkExperience exp = new WorkExperience();
                    exp.setResumeId(resumeId);
                    exp.setJobTitle(jobTitles[i]);
                    exp.setCompany(req.getParameterValues("company")[i]);
                    String startDateStr = req.getParameterValues("startDate")[i];
                    if (startDateStr != null && !startDateStr.isEmpty()) exp.setStartDate(Date.valueOf(startDateStr));
                    String endDateStr = req.getParameterValues("endDate")[i];
                    if (endDateStr != null && !endDateStr.isEmpty()) exp.setEndDate(Date.valueOf(endDateStr));
                    exp.setLocation(req.getParameterValues("location")[i]);
                    exp.setDescription(req.getParameterValues("exp_description")[i]);
                    expDAO.addWorkExperience(exp);
                }
            }

            // === 3. Update Education (Delete and Re-insert) ===
            EducationDAO eduDAO = new EducationDAO(conn);
            eduDAO.deleteEducationByResumeId(resumeId);
            String[] institutions = req.getParameterValues("institution");
            if (institutions != null) {
                for (int i = 0; i < institutions.length; i++) {
                    if (institutions[i] == null || institutions[i].trim().isEmpty()) continue;
                    Education edu = new Education();
                    edu.setResumeId(resumeId);
                    edu.setInstitution(institutions[i]);
                    edu.setDegree(req.getParameterValues("degree")[i]);
                    edu.setFieldOfStudy(req.getParameterValues("fieldOfStudy")[i]);
                    String gradYearStr = req.getParameterValues("gradYear")[i];
                    if (gradYearStr != null && !gradYearStr.isEmpty()) edu.setGraduationYear(Integer.parseInt(gradYearStr));
                    eduDAO.addEducation(edu);
                }
            }

            // === 4. Update Skills (Delete and Re-insert) ===
            SkillDAO skillDAO = new SkillDAO(conn);
            skillDAO.deleteSkillsByResumeId(resumeId);
            String[] skillNames = req.getParameterValues("skillName");
            if (skillNames != null) {
                for (int i = 0; i < skillNames.length; i++) {
                    if (skillNames[i] == null || skillNames[i].trim().isEmpty()) continue;
                    Skill skill = new Skill();
                    skill.setResumeId(resumeId);
                    skill.setSkillName(skillNames[i]);
                    skill.setSkillLevel(req.getParameterValues("skillLevel")[i]);
                    skillDAO.addSkill(skill);
                }
            }
            
            // === 5. Update Projects (Delete and Re-insert) ===
            ProjectDAO projectDAO = new ProjectDAO(conn);
            projectDAO.deleteProjectsByResumeId(resumeId);
            String[] projectTitles = req.getParameterValues("projectTitle");
            if (projectTitles != null) {
                 for (int i = 0; i < projectTitles.length; i++) {
                    if (projectTitles[i] == null || projectTitles[i].trim().isEmpty()) continue;
                    Project proj = new Project();
                    proj.setResumeId(resumeId);
                    proj.setProjectTitle(projectTitles[i]);
                    proj.setProjectLink(req.getParameterValues("projectLink")[i]);
                    proj.setDescription(req.getParameterValues("proj_description")[i]);
                    projectDAO.addProject(proj);
                }
            }

            // === 6. Update Certifications (Delete and Re-insert) ===
            CertificationDAO certDAO = new CertificationDAO(conn);
            certDAO.deleteCertificationsByResumeId(resumeId);
            String[] certNames = req.getParameterValues("certName");
            if(certNames != null) {
                for(int i = 0; i < certNames.length; i++) {
                    if (certNames[i] == null || certNames[i].trim().isEmpty()) continue;
                    Certification cert = new Certification();
                    cert.setResumeId(resumeId);
                    cert.setCertName(certNames[i]);
                    cert.setCertOrg(req.getParameterValues("certOrg")[i]);
                    certDAO.addCertification(cert);
                }
            }

            // === 7. Update Achievements (Delete and Re-insert) ===
            AchievementDAO achievementDAO = new AchievementDAO(conn);
            achievementDAO.deleteAchievementsByResumeId(resumeId);
            String[] achievementDescs = req.getParameterValues("achievementDesc");
            if(achievementDescs != null) {
                for(String desc : achievementDescs) {
                    if(desc == null || desc.trim().isEmpty()) continue;
                    Achievement ach = new Achievement();
                    ach.setResumeId(resumeId);
                    ach.setDescription(desc);
                    achievementDAO.addAchievement(ach);
                }
            }
            
            // === 8. Update Links (Delete and Re-insert) ===
            LinkDAO linkDAO = new LinkDAO(conn);
            linkDAO.deleteLinksByResumeId(resumeId);
            String[] linkNames = req.getParameterValues("linkName");
            if(linkNames != null) {
                for(int i = 0; i < linkNames.length; i++) {
                    if(linkNames[i] == null || linkNames[i].trim().isEmpty()) continue;
                    Link link = new Link();
                    link.setResumeId(resumeId);
                    link.setLinkName(linkNames[i]);
                    link.setLinkUrl(req.getParameterValues("linkUrl")[i]);
                    linkDAO.addLink(link);
                }
            }


            conn.commit(); // Finalize all changes if successful
            session.setAttribute("successMsg", "Resume Updated Successfully!");
            resp.sendRedirect("mydocuments.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Undo changes on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            session.setAttribute("errorMsg", "An error occurred while updating the resume.");
            if (resumeId != -1) {
                resp.sendRedirect("edit_resume.jsp?id=" + resumeId);
            } else {
                resp.sendRedirect("mydocuments.jsp");
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}