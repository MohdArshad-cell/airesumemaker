package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.entity.Resume;

public class ResumeDAO {

    private Connection conn;

    public ResumeDAO(Connection conn) {
        super();
        this.conn = conn;
    }

    /**
     * Adds a new resume to the database and returns the auto-generated resume_id.
     * @param resume The Resume object to add.
     * @return The new resume_id if successful, or -1 if it fails.
     */
    public int addResume(Resume resume) {
        int resumeId = -1;
        try {
            String sql = "INSERT INTO resumes(user_id, title, full_name, email, phone, address, summary) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, resume.getUserId());
            ps.setString(2, resume.getTitle());
            ps.setString(3, resume.getFullName());
            ps.setString(4, resume.getEmail());
            ps.setString(5, resume.getPhone());
            ps.setString(6, resume.getAddress());
            ps.setString(7, resume.getSummary());

            int i = ps.executeUpdate();
            
            if (i == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    resumeId = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resumeId;
    }

    /**
     * Fetches all resumes for a specific user.
     * @param userId The ID of the user.
     * @return A list of Resume objects.
     */
    public List<Resume> getResumesByUserId(int userId) {
        List<Resume> resumeList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM resumes WHERE user_id = ? ORDER BY created_at DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Resume resume = new Resume();
                // These lines MUST be here to populate the object
                resume.setResumeId(rs.getInt("resume_id"));
                resume.setUserId(rs.getInt("user_id"));
                resume.setTitle(rs.getString("title"));
                resume.setFullName(rs.getString("full_name"));
                resume.setEmail(rs.getString("email"));
                resume.setPhone(rs.getString("phone"));
                resume.setAddress(rs.getString("address"));
                resume.setSummary(rs.getString("summary"));
                resume.setCreatedAt(rs.getTimestamp("created_at"));
                
                resumeList.add(resume);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resumeList;
    }

    /**
     * Fetches a single resume by its ID, ensuring it belongs to the logged-in user.
     * @param resumeId The ID of the resume to fetch.
     * @param userId The ID of the currently logged-in user (for security).
     * @return A Resume object, or null if not found or not owned by the user.
     */
    public Resume getResumeById(int resumeId, int userId) {
        Resume resume = null;
        try {
            // The "AND user_id = ?" part is a security measure
            String sql = "SELECT * FROM resumes WHERE resume_id = ? AND user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                resume = new Resume();
                
                // --- This is the completed code ---
                resume.setResumeId(rs.getInt("resume_id"));
                resume.setUserId(rs.getInt("user_id"));
                resume.setTitle(rs.getString("title"));
                resume.setFullName(rs.getString("full_name"));
                resume.setEmail(rs.getString("email"));
                resume.setPhone(rs.getString("phone"));
                resume.setAddress(rs.getString("address"));
                resume.setSummary(rs.getString("summary"));
                resume.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resume;
    }
    
    /**
     * Updates an existing resume in the database.
     * @param resume The Resume object with updated information.
     * @return true if successful, false otherwise.
     */
    public boolean updateResume(Resume resume) {
        boolean f = false;
        try {
            String sql = "UPDATE resumes SET title=?, full_name=?, email=?, phone=?, address=?, summary=? WHERE resume_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            // Set all parameters for the UPDATE statement
            ps.setString(1, resume.getTitle());
            ps.setString(2, resume.getFullName());
            ps.setString(3, resume.getEmail());
            ps.setString(4, resume.getPhone());
            ps.setString(5, resume.getAddress());
            ps.setString(6, resume.getSummary());
            ps.setInt(7, resume.getResumeId());
            
            if (ps.executeUpdate() == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * Deletes a resume from the database.
     * @param resumeId The ID of the resume to delete.
     * @return true if successful, false otherwise.
     */
    public boolean deleteResume(int resumeId, int userId) {
        boolean f = false;
        try {
            String sql = "DELETE FROM resumes WHERE resume_id = ? AND user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();
            if (rows == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }


}