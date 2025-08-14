package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.entity.Project;

public class ProjectDAO {

    private Connection conn;

    /**
     * Constructor that takes a database connection.
     * @param conn The active database connection.
     */
    public ProjectDAO(Connection conn) {
        super();
        this.conn = conn;
    }

    /**
     * Adds a new project record to the database.
     * This is used by the SaveResumeServlet.
     * @param proj The Project object containing the data to be saved.
     * @return true if the project was added successfully, false otherwise.
     */
    public boolean addProject(Project proj) {
        boolean f = false;
        try {
            String sql = "INSERT INTO projects(resume_id, project_title, project_link, description) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, proj.getResumeId());
            ps.setString(2, proj.getProjectTitle());
            ps.setString(3, proj.getProjectLink());
            ps.setString(4, proj.getDescription());

            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * Fetches a list of all projects associated with a specific resume ID.
     * This is used in the edit_resume.jsp page to display existing data.
     * @param resumeId The ID of the resume to fetch projects for.
     * @return A list of Project objects.
     */
    public List<Project> getProjectsByResumeId(int resumeId) {
        List<Project> list = new ArrayList<>();
        Project proj = null;

        try {
            String sql = "SELECT * FROM projects WHERE resume_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                proj = new Project();
                proj.setProjectId(rs.getInt("project_id"));
                proj.setResumeId(rs.getInt("resume_id"));
                proj.setProjectTitle(rs.getString("project_title"));
                proj.setProjectLink(rs.getString("project_link"));
                proj.setDescription(rs.getString("description"));
                list.add(proj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Deletes all projects associated with a specific resume ID.
     * This is a helper method for the UpdateResumeServlet. The typical update
     * process is to delete all old entries and then re-add the updated list.
     * @param resumeId The ID of the resume whose projects should be deleted.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteProjectsByResumeId(int resumeId) {
        boolean f = false;
        try {
            String sql = "DELETE FROM projects WHERE resume_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);
            
            ps.executeUpdate();
            f = true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
}