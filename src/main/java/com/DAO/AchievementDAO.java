package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.entity.Achievement;

public class AchievementDAO {

    private Connection conn;

    /**
     * Constructor that takes a database connection.
     * @param conn The active database connection.
     */
    public AchievementDAO(Connection conn) {
        super();
        this.conn = conn;
    }

    /**
     * Adds a new achievement record to the database.
     * This is used when creating a new resume.
     * @param ach The Achievement object containing the data to be saved.
     * @return true if the achievement was added successfully, false otherwise.
     */
    public boolean addAchievement(Achievement ach) {
        boolean f = false;
        try {
            String sql = "INSERT INTO achievements(resume_id, description) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ach.getResumeId());
            ps.setString(2, ach.getDescription());

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
     * Fetches a list of all achievements associated with a specific resume ID.
     * This is used in the edit_resume.jsp page to display existing achievements.
     * @param resumeId The ID of the resume to fetch achievements for.
     * @return A list of Achievement objects.
     */
    public List<Achievement> getAchievementsByResumeId(int resumeId) {
        List<Achievement> list = new ArrayList<Achievement>();
        Achievement ach = null;

        try {
            String sql = "SELECT * FROM achievements WHERE resume_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ach = new Achievement();
                ach.setAchievementId(rs.getInt(1));
                ach.setResumeId(rs.getInt(2));
                ach.setDescription(rs.getString(3));
                list.add(ach);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Deletes all achievements associated with a specific resume ID.
     * This is useful for the UpdateResumeServlet, where you can delete all old
     * entries before adding the updated ones from the form.
     * @param resumeId The ID of the resume whose achievements should be deleted.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteAchievementsByResumeId(int resumeId) {
        boolean f = false;
        try {
            String sql = "DELETE FROM achievements WHERE resume_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);
            
            int i = ps.executeUpdate();
            if (i > 0) {
                f = true; // Returns true even if 0 rows were deleted, as the operation succeeded
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
}