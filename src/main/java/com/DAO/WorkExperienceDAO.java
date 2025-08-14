package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.entity.WorkExperience;

public class WorkExperienceDAO {

    private Connection conn;

    public WorkExperienceDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Adds a new work experience entry to the database.
     * @param exp The WorkExperience object to be saved.
     * @return true if the operation is successful, false otherwise.
     */
    public boolean addWorkExperience(WorkExperience exp) {
        boolean f = false;
        try {
            String sql = "INSERT INTO work_experience(resume_id, job_title, company, location, start_date, end_date, description) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, exp.getResumeId());
            ps.setString(2, exp.getJobTitle());
            ps.setString(3, exp.getCompany());
            ps.setString(4, exp.getLocation());
            ps.setDate(5, exp.getStartDate());
            ps.setDate(6, exp.getEndDate());
            ps.setString(7, exp.getDescription());

            if (ps.executeUpdate() == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * Retrieves all work experience entries associated with a specific resume ID.
     * @param resumeId The ID of the resume.
     * @return A list of WorkExperience objects.
     */
    public List<WorkExperience> getWorkExperienceByResumeId(int resumeId) {
        List<WorkExperience> list = new ArrayList<>();
        WorkExperience exp = null;
        try {
            String sql = "SELECT * FROM work_experience WHERE resume_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                exp = new WorkExperience();
                exp.setExperienceId(rs.getInt("experience_id"));
                exp.setResumeId(rs.getInt("resume_id"));
                exp.setJobTitle(rs.getString("job_title"));
                exp.setCompany(rs.getString("company"));
                exp.setLocation(rs.getString("location"));
                exp.setStartDate(rs.getDate("start_date"));
                exp.setEndDate(rs.getDate("end_date"));
                exp.setDescription(rs.getString("description"));
                list.add(exp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // You can add update and delete methods here in the future if needed.
    /**
     * Deletes all work experience entries for a specific resume ID.
     * @param resumeId The ID of the resume whose experience entries should be deleted.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean deleteWorkExperienceByResumeId(int resumeId) {
        boolean f = false;
        try {
            // Assuming your table name is 'work_experience'
            String sql = "DELETE FROM work_experience WHERE resume_id = ?";
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