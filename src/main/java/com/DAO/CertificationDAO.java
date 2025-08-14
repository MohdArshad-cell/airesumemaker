package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.entity.Certification;

public class CertificationDAO {

    private Connection conn;

    /**
     * Constructor that takes a database connection.
     * @param conn The active database connection.
     */
    public CertificationDAO(Connection conn) {
        super();
        this.conn = conn;
    }

    /**
     * Adds a new certification record to the database.
     * This is used by the SaveResumeServlet.
     * @param cert The Certification object containing the data to be saved.
     * @return true if the certification was added successfully, false otherwise.
     */
    public boolean addCertification(Certification cert) {
        boolean f = false;
        try {
            String sql = "INSERT INTO certifications(resume_id, cert_name, cert_org) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cert.getResumeId());
            ps.setString(2, cert.getCertName());
            ps.setString(3, cert.getCertOrg());

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
     * Fetches a list of all certifications associated with a specific resume ID.
     * This is used in the edit_resume.jsp page to display existing data.
     * @param resumeId The ID of the resume to fetch certifications for.
     * @return A list of Certification objects.
     */
    public List<Certification> getCertificationsByResumeId(int resumeId) {
        List<Certification> list = new ArrayList<>();
        Certification cert = null;

        try {
            String sql = "SELECT * FROM certifications WHERE resume_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cert = new Certification();
                cert.setCertId(rs.getInt("cert_id"));
                cert.setResumeId(rs.getInt("resume_id"));
                cert.setCertName(rs.getString("cert_name"));
                cert.setCertOrg(rs.getString("cert_org"));
                list.add(cert);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Deletes all certifications associated with a specific resume ID.
     * This is a helper method for the UpdateResumeServlet. The typical update
     * process is to delete all old entries and then re-add the updated list.
     * @param resumeId The ID of the resume whose certifications should be deleted.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteCertificationsByResumeId(int resumeId) {
        boolean f = false;
        try {
            String sql = "DELETE FROM certifications WHERE resume_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);
            
            // executeUpdate() returns the number of rows affected.
            // We can consider the operation successful even if 0 rows are deleted.
            ps.executeUpdate();
            f = true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
}