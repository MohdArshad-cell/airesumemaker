package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.entity.Link;

public class LinkDAO {

    private Connection conn;

    /**
     * Constructor that takes a database connection.
     * @param conn The active database connection.
     */
    public LinkDAO(Connection conn) {
        super();
        this.conn = conn;
    }

    /**
     * Adds a new link record to the database.
     * This is used by the SaveResumeServlet.
     * @param link The Link object containing the data to be saved.
     * @return true if the link was added successfully, false otherwise.
     */
    public boolean addLink(Link link) {
        boolean f = false;
        try {
            String sql = "INSERT INTO links(resume_id, link_name, link_url) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, link.getResumeId());
            ps.setString(2, link.getLinkName());
            ps.setString(3, link.getLinkUrl());

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
     * Fetches a list of all links associated with a specific resume ID.
     * This is used in the edit_resume.jsp page to display existing data.
     * @param resumeId The ID of the resume to fetch links for.
     * @return A list of Link objects.
     */
    public List<Link> getLinksByResumeId(int resumeId) {
        List<Link> list = new ArrayList<>();
        Link link = null;

        try {
            String sql = "SELECT *  FROM social_links WHERE resume_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                link = new Link();
                link.setLinkId(rs.getInt("link_id"));
                link.setResumeId(rs.getInt("resume_id"));
                link.setLinkName(rs.getString("link_name"));
                link.setLinkUrl(rs.getString("link_url"));
                list.add(link);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Deletes all links associated with a specific resume ID.
     * This is a helper method for the UpdateResumeServlet. The typical update
     * process is to delete all old entries and then re-add the updated list.
     * @param resumeId The ID of the resume whose links should be deleted.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteLinksByResumeId(int resumeId) {
        boolean f = false;
        try {
            String sql = "DELETE FROM links WHERE resume_id = ?";
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