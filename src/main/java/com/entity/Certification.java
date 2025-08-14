package com.entity;

/**
 * Represents a single certification entry in a resume.
 * This class is a POJO (Plain Old Java Object) to hold certification data.
 */
public class Certification {

    private int certId;
    private int resumeId;
    private String certName;
    private String certOrg; // Issuing Organization

    /**
     * Default constructor.
     */
    public Certification() {
        super();
    }

    // --- Getters and Setters ---

    public int getCertId() {
        return certId;
    }

    public void setCertId(int certId) {
        this.certId = certId;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getCertOrg() {
        return certOrg;
    }

    public void setCertOrg(String certOrg) {
        this.certOrg = certOrg;
    }

    // --- toString() method for debugging ---

    @Override
    public String toString() {
        return "Certification [certId=" + certId + ", resumeId=" + resumeId + ", certName=" + certName + ", certOrg="
                + certOrg + "]";
    }
}