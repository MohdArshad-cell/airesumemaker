package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.entity.Education;

public class EducationDAO {

    private Connection conn;

    public EducationDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Adds a new education entry to the database.
     * @param edu The Education object to be saved.
     * @return true if the operation is successful, false otherwise.
     */
    public boolean addEducation(Education edu) {
        boolean f = false;
        try {
            String sql = "INSERT INTO education(resume_id, institution, degree, field_of_study, graduation_year) VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, edu.getResumeId());
            ps.setString(2, edu.getInstitution());
            ps.setString(3, edu.getDegree());
            ps.setString(4, edu.getFieldOfStudy());
            ps.setInt(5, edu.getGraduationYear());

            if (ps.executeUpdate() == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * Retrieves all education entries associated with a specific resume ID.
     * @param resumeId The ID of the resume.
     * @return A list of Education objects.
     */
    public List<Education> getEducationByResumeId(int resumeId) {
        List<Education> list = new ArrayList<>();
        Education edu = null;
        try {
            String sql = "SELECT * FROM education WHERE resume_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                edu = new Education();
                edu.setEducationId(rs.getInt("education_id"));
                edu.setResumeId(rs.getInt("resume_id"));
                edu.setInstitution(rs.getString("institution"));
                edu.setDegree(rs.getString("degree"));
                edu.setFieldOfStudy(rs.getString("field_of_study"));
                edu.setGraduationYear(rs.getInt("graduation_year"));
                list.add(edu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // You can add update and delete methods here in the future if needed
    /**
     * Deletes all education entries for a specific resume ID.
     * @param resumeId The ID of the resume whose education entries should be deleted.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean deleteEducationByResumeId(int resumeId) {
        boolean f = false;
        try {
            String sql = "DELETE FROM education WHERE resume_id = ?";
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