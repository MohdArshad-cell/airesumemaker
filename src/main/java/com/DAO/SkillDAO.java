package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.entity.Skill;

public class SkillDAO {

    private Connection conn;

    public SkillDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Adds a new skill to the database for a specific resume.
     * @param skill The Skill object to be saved.
     * @return true if the operation is successful, false otherwise.
     */
    public boolean addSkill(Skill skill) {
        boolean f = false;
        try {
            String sql = "INSERT INTO skills(resume_id, skill_name, skill_level) VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, skill.getResumeId());
            ps.setString(2, skill.getSkillName());
            ps.setString(3, skill.getSkillLevel());

            if (ps.executeUpdate() == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * Retrieves all skills associated with a specific resume ID.
     * @param resumeId The ID of the resume.
     * @return A list of Skill objects.
     */
    public List<Skill> getSkillsByResumeId(int resumeId) {
        List<Skill> list = new ArrayList<>();
        Skill skill = null;
        try {
            String sql = "SELECT * FROM skills WHERE resume_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                skill = new Skill();
                skill.setSkillId(rs.getInt("skill_id"));
                skill.setResumeId(rs.getInt("resume_id"));
                skill.setSkillName(rs.getString("skill_name"));
                skill.setSkillLevel(rs.getString("skill_level"));
                list.add(skill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // You can add update and delete methods here in the future if needed.
    /**
     * Deletes all skills associated with a specific resume ID.
     * @param resumeId The ID of the resume whose skills should be deleted.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean deleteSkillsByResumeId(int resumeId) {
        boolean f = false;
        try {
            String sql = "DELETE FROM skills WHERE resume_id = ?";
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