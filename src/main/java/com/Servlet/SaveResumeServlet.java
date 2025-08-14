package com.Servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import com.DAO.AchievementDAO;
import com.DAO.CertificationDAO;
import com.DAO.EducationDAO;
import com.DAO.LinkDAO;
import com.DAO.ProjectDAO;
import com.DAO.ResumeDAO;
import com.DAO.SkillDAO;
import com.DAO.WorkExperienceDAO;
import com.Db.DBConnect;
import com.entity.Achievement;
import com.entity.Certification;
import com.entity.Education;
import com.entity.Link;
import com.entity.Project;
import com.entity.Resume;
import com.entity.Skill;
import com.entity.User;
import com.entity.WorkExperience;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/saveResume")
public class SaveResumeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            session.setAttribute("errorMsg", "You must be logged in to save a resume.");
            resp.sendRedirect("login.jsp");
            return;
        }

        Connection conn = null;

        try {
            conn = DBConnect.getConn();
            conn.setAutoCommit(false); // Start a database transaction

            // --- 1. Save the main resume details ---
            Resume resume = new Resume();
            resume.setUserId(user.getUserId());
            resume.setTitle(req.getParameter("title"));
            resume.setFullName(req.getParameter("fullName"));
            resume.setEmail(req.getParameter("email"));
            resume.setPhone(req.getParameter("phone"));
            resume.setAddress(req.getParameter("address"));
            resume.setSummary(req.getParameter("summary"));

            ResumeDAO resumeDAO = new ResumeDAO(conn);
            int resumeId = resumeDAO.addResume(resume);

            if (resumeId <= 0) {
                throw new SQLException("Failed to save main resume details, no ID was generated.");
            }

            // --- 2. Save Work Experience entries ---
            WorkExperienceDAO expDAO = new WorkExperienceDAO(conn);
            String[] jobTitles = req.getParameterValues("jobTitle");
            if (jobTitles != null) {
                String[] companies = req.getParameterValues("company");
                String[] startDates = req.getParameterValues("startDate");
                String[] endDates = req.getParameterValues("endDate");
                String[] locations = req.getParameterValues("location");
                String[] expDescriptions = req.getParameterValues("exp_description");
                
                for (int i = 0; i < jobTitles.length; i++) {
                    if (jobTitles[i] == null || jobTitles[i].trim().isEmpty()) continue;
                    
                    WorkExperience exp = new WorkExperience();
                    exp.setResumeId(resumeId);
                    exp.setJobTitle(jobTitles[i]);
                    exp.setCompany(companies[i]);
                    if (startDates[i] != null && !startDates[i].isEmpty()) exp.setStartDate(Date.valueOf(startDates[i]));
                    if (endDates[i] != null && !endDates[i].isEmpty()) exp.setEndDate(Date.valueOf(endDates[i]));
                    exp.setLocation(locations[i]);
                    exp.setDescription(expDescriptions[i]); 
                    expDAO.addWorkExperience(exp);
                }
            }

            // --- 3. Save Education entries ---
            EducationDAO eduDAO = new EducationDAO(conn);
            String[] institutions = req.getParameterValues("institution");
            if (institutions != null) {
                String[] degrees = req.getParameterValues("degree");
                String[] fieldsOfStudy = req.getParameterValues("fieldOfStudy");
                String[] gradYears = req.getParameterValues("gradYear");

                for (int i = 0; i < institutions.length; i++) {
                     if (institutions[i] == null || institutions[i].trim().isEmpty()) continue;
                    
                    Education edu = new Education();
                    edu.setResumeId(resumeId);
                    edu.setInstitution(institutions[i]);
                    edu.setDegree(degrees[i]);
                    edu.setFieldOfStudy(fieldsOfStudy[i]);
                    if (gradYears[i] != null && !gradYears[i].isEmpty()) edu.setGraduationYear(Integer.parseInt(gradYears[i]));
                    eduDAO.addEducation(edu);
                }
            }

            // --- 4. Save Skill entries ---
            SkillDAO skillDAO = new SkillDAO(conn);
            String[] skillNames = req.getParameterValues("skillName");
            if (skillNames != null) {
                 String[] skillLevels = req.getParameterValues("skillLevel");
                for (int i = 0; i < skillNames.length; i++) {
                     if (skillNames[i] == null || skillNames[i].trim().isEmpty()) continue;
                    
                    Skill skill = new Skill();
                    skill.setResumeId(resumeId);
                    skill.setSkillName(skillNames[i]);
                    skill.setSkillLevel(skillLevels[i]); 
                    skillDAO.addSkill(skill);
                }
            }
            
            // --- 5. (NEW) Save Project entries ---
            ProjectDAO projectDAO = new ProjectDAO(conn);
            String[] projectTitles = req.getParameterValues("projectTitle");
            if(projectTitles != null) {
                String[] projectLinks = req.getParameterValues("projectLink");
                String[] projDescriptions = req.getParameterValues("proj_description");
                for(int i = 0; i < projectTitles.length; i++) {
                    if (projectTitles[i] == null || projectTitles[i].trim().isEmpty()) continue;
                    
                    Project proj = new Project();
                    proj.setResumeId(resumeId);
                    proj.setProjectTitle(projectTitles[i]);
                    proj.setProjectLink(projectLinks[i]);
                    proj.setDescription(projDescriptions[i]);
                    projectDAO.addProject(proj);
                }
            }

            // --- 6. (NEW) Save Certification entries ---
            CertificationDAO certDAO = new CertificationDAO(conn);
            String[] certNames = req.getParameterValues("certName");
            if(certNames != null) {
                String[] certOrgs = req.getParameterValues("certOrg");
                for(int i = 0; i < certNames.length; i++) {
                    if (certNames[i] == null || certNames[i].trim().isEmpty()) continue;
                    
                    Certification cert = new Certification();
                    cert.setResumeId(resumeId);
                    cert.setCertName(certNames[i]);
                    cert.setCertOrg(certOrgs[i]);
                    certDAO.addCertification(cert);
                }
            }

            // --- 7. (NEW) Save Achievement entries ---
            AchievementDAO achievementDAO = new AchievementDAO(conn);
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
            
            // --- 8. (NEW) Save Link entries ---
            LinkDAO linkDAO = new LinkDAO(conn);
            String[] linkNames = req.getParameterValues("linkName");
            if(linkNames != null) {
                String[] linkUrls = req.getParameterValues("linkUrl");
                for(int i = 0; i < linkNames.length; i++) {
                    if(linkNames[i] == null || linkNames[i].trim().isEmpty()) continue;
                    
                    Link link = new Link();
                    link.setResumeId(resumeId);
                    link.setLinkName(linkNames[i]);
                    link.setLinkUrl(linkUrls[i]);
                    linkDAO.addLink(link);
                }
            }

            conn.commit(); // If everything was successful, commit the transaction
            session.setAttribute("successMsg", "Resume Saved Successfully!");
            resp.sendRedirect("mydocuments.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            session.setAttribute("errorMsg", "An error occurred while saving: " + e.getMessage());
            resp.sendRedirect("create_scratch.jsp");
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