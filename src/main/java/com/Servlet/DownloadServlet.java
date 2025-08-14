package com.Servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.DAO.*;
import com.Db.DBConnect;
import com.entity.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            session.setAttribute("errorMsg", "Please login to download a resume.");
            resp.sendRedirect("login.jsp");
            return;
        }

        int resumeId = -1;
        try {
            resumeId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            session.setAttribute("errorMsg", "Invalid Resume ID.");
            resp.sendRedirect("mydocuments.jsp");
            return;
        }
        
        Path tempDir = Files.createTempDirectory("resume-gen-");
        // ADDED: Crucial line for debugging. This will print the path in your Eclipse console.
        System.out.println("---- LaTeX temporary files are in: " + tempDir.toString() + " ----");
        File pdfFile = null;

        try (Connection conn = DBConnect.getConn()) {
            // === 1. Fetch all data for the resume ===
            ResumeDAO resumeDAO = new ResumeDAO(conn);
            Resume resume = resumeDAO.getResumeById(resumeId, user.getUserId());

            if (resume == null) {
                session.setAttribute("errorMsg", "Resume not found or you don't have permission.");
                resp.sendRedirect("mydocuments.jsp");
                return;
            }

            WorkExperienceDAO expDAO = new WorkExperienceDAO(conn);
            List<WorkExperience> expList = expDAO.getWorkExperienceByResumeId(resumeId);
            EducationDAO eduDAO = new EducationDAO(conn);
            List<Education> eduList = eduDAO.getEducationByResumeId(resumeId);
            SkillDAO skillDAO = new SkillDAO(conn);
            List<Skill> skillList = skillDAO.getSkillsByResumeId(resumeId);
            ProjectDAO projectDAO = new ProjectDAO(conn);
            List<Project> projectList = projectDAO.getProjectsByResumeId(resumeId);
            CertificationDAO certDAO = new CertificationDAO(conn);
            List<Certification> certList = certDAO.getCertificationsByResumeId(resumeId);
            AchievementDAO achievementDAO = new AchievementDAO(conn);
            List<Achievement> achievementList = achievementDAO.getAchievementsByResumeId(resumeId);
            LinkDAO linkDAO = new LinkDAO(conn);
            List<Link> linkList = linkDAO.getLinksByResumeId(resumeId);
            
            // === 2. Generate LaTeX Content ===
            // FIXED: Method call now passes all the lists.
            String latexContent = generateLatex(resume, expList, eduList, skillList, projectList, certList, achievementList, linkList);
            Path texPath = tempDir.resolve("resume.tex");
            Files.write(texPath, latexContent.getBytes());

            // === 3. Compile LaTeX to PDF ===
            boolean compiled = compileLatex(texPath);
            if (!compiled) {
                throw new Exception("Failed to compile the LaTeX document. Check latex_output.log in the temp directory.");
            }
            
            pdfFile = new File(tempDir.toFile(), "resume.pdf");
            if (!pdfFile.exists()) {
                 throw new Exception("PDF file was not created. Check latex_output.log for errors.");
            }

            // === 4. Stream PDF to User ===
            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + resume.getTitle().replaceAll("[^a-zA-Z0-9.-]", "_") + ".pdf\"");
            resp.setContentLength((int) pdfFile.length());

            try (InputStream in = new FileInputStream(pdfFile); OutputStream out = resp.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Could not generate PDF: " + e.getMessage());
            resp.sendRedirect("mydocuments.jsp");
        } 
    }
    
    /**
     * Executes the pdflatex command to compile a .tex file into a .pdf file.
     * FIXED: This version correctly handles paths with spaces.
     */
    private boolean compileLatex(Path texPath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
            "pdflatex",
            "-interaction=nonstopmode",
            texPath.getFileName().toString() // Pass only the filename e.g., "resume.tex"
        );

        // Set the working directory for the command to be the temporary folder.
        // This is the correct way to handle paths with spaces.
        pb.directory(texPath.getParent().toFile());

        // Logging remains the same
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.to(new File(texPath.getParent().toFile(), "latex_output.log")));

        Process process = pb.start();
        // We run it twice to ensure all references (like page numbers) are correct
        process.waitFor(30, TimeUnit.SECONDS); 
        process = pb.start();
        return process.waitFor(30, TimeUnit.SECONDS);
    }

    private String escapeLatex(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\textbackslash{}").replace("&", "\\&").replace("%", "\\%").replace("$", "\\$")
                .replace("#", "\\#").replace("_", "\\_").replace("{", "\\{").replace("}", "\\}")
                .replace("~", "\\textasciitilde{}").replace("^", "\\textasciicircum{}");
    }

    // FIXED: Method signature and body are now complete.
    private String generateLatex(Resume r, List<WorkExperience> exps, List<Education> edus, List<Skill> skills, List<Project> projects, List<Certification> certs, List<Achievement> achievements, List<Link> links) {
        StringBuilder sb = new StringBuilder();
        sb.append("\\documentclass[a4paper,11pt]{article}\n");
        sb.append("\\usepackage[left=0.75in,top=0.6in,right=0.75in,bottom=0.6in]{geometry}\n");
        sb.append("\\usepackage{titlesec}\n\\usepackage{enumitem}\n\\usepackage[hidelinks]{hyperref}\n");
        sb.append("\\titleformat{\\section}{\\Large\\scshape\\raggedright}{}{0em}{}[\\titlerule]\n");
        sb.append("\\titlespacing{\\section}{0pt}{2ex}{1ex}\n\\pagestyle{empty}\n");
        sb.append("\\begin{document}\n\n");

        StringBuilder contactLine = new StringBuilder();
        contactLine.append(escapeLatex(r.getAddress()) + " $\\cdot$ " + escapeLatex(r.getPhone()) + " $\\cdot$ \\href{mailto:" + r.getEmail() + "}{" + escapeLatex(r.getEmail()) + "}");
        if (links != null) for(Link link : links) contactLine.append(" $\\cdot$ \\href{" + escapeLatex(link.getLinkUrl()) + "}{" + escapeLatex(link.getLinkName()) + "}");
        sb.append("\\begin{center}\n{\\Huge\\scshape " + escapeLatex(r.getFullName()) + "}\\vspace{1.5ex}\n" + contactLine.toString() + "\n\\end{center}\n\n");
        
        if (r.getSummary() != null && !r.getSummary().isEmpty()) sb.append("\\section*{Summary}\n" + escapeLatex(r.getSummary()) + "\n\n");
        
        if (exps != null && !exps.isEmpty()) {
            sb.append("\\section{Experience}\n\\begin{itemize}[leftmargin=*, label={}]\n");
            for (WorkExperience exp : exps) {
                sb.append("    \\item \\textbf{" + escapeLatex(exp.getJobTitle()) + "} $\\vert$ \\textit{" + escapeLatex(exp.getCompany()) + "}");
                if(exp.getStartDate() != null && exp.getEndDate() != null) sb.append(" \\hfill {" + exp.getStartDate() + " -- " + exp.getEndDate() + "}\n");
                else sb.append("\n");
                sb.append("    \\begin{itemize}[label=$\\cdot$]\n");
                if(exp.getDescription() != null) for(String item : exp.getDescription().split("\\r?\\n")) if(!item.trim().isEmpty()) sb.append("        \\item " + escapeLatex(item.trim()) + "\n");
                sb.append("    \\end{itemize}\n");
            }
            sb.append("\\end{itemize}\n\n");
        }
        
        if(projects != null && !projects.isEmpty()){
            sb.append("\\section{Projects}\n\\begin{itemize}[leftmargin=*, label={}]\n");
            for(Project proj : projects){
                sb.append("    \\item \\textbf{" + escapeLatex(proj.getProjectTitle()) + "}");
                if(proj.getProjectLink() != null && !proj.getProjectLink().isEmpty()) sb.append(" $\\vert$ \\href{" + escapeLatex(proj.getProjectLink()) + "}{View Project}");
                sb.append("\n    \\begin{itemize}[label=$\\cdot$]\n");
                if(proj.getDescription() != null) for(String item : proj.getDescription().split("\\r?\\n")) if(!item.trim().isEmpty()) sb.append("        \\item " + escapeLatex(item.trim()) + "\n");
                sb.append("    \\end{itemize}\n");
            }
             sb.append("\\end{itemize}\n\n");
        }

        if (edus != null && !edus.isEmpty()) {
            sb.append("\\section{Education}\n");
            for (Education edu : edus) {
                sb.append("\\textbf{" + escapeLatex(edu.getInstitution()) + "} \\hfill \\textbf{" + edu.getGraduationYear() + "}\n\n");
                sb.append("\\textit{" + escapeLatex(edu.getDegree()) + " in " + escapeLatex(edu.getFieldOfStudy()) + "}\n\n");
            }
        }
        
        if (skills != null && !skills.isEmpty()) {
             sb.append("\\section{Skills}\n");
             StringBuilder skillsLine = new StringBuilder();
             for(int i=0; i < skills.size(); i++) {
                 skillsLine.append(escapeLatex(skills.get(i).getSkillName()));
                 if(i < skills.size() - 1) skillsLine.append(" $\\cdot$ ");
             }
             sb.append(skillsLine.toString() + "\n\n");
        }

        if (certs != null && !certs.isEmpty()) {
            sb.append("\\section{Certifications}\n\\begin{itemize}[label=$\\bullet$]\n");
            for (Certification cert : certs) sb.append("    \\item " + escapeLatex(cert.getCertName()) + " -- \\textit{" + escapeLatex(cert.getCertOrg()) + "}\n");
            sb.append("\\end{itemize}\n\n");
        }

        if (achievements != null && !achievements.isEmpty()) {
            sb.append("\\section{Achievements}\n\\begin{itemize}[label=$\\bullet$]\n");
            for (Achievement ach : achievements) sb.append("    \\item " + escapeLatex(ach.getDescription()) + "\n");
            sb.append("\\end{itemize}\n\n");
        }

        sb.append("\\end{document}\n");
        return sb.toString();
    }
}