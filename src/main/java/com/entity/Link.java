package com.entity;

/**
 * Represents a single web link entry in a resume (e.g., LinkedIn, GitHub).
 * This class is a POJO (Plain Old Java Object) to hold link data.
 */
public class Link {

    private int linkId;
    private int resumeId;
    private String linkName;
    private String linkUrl;

    /**
     * Default constructor.
     */
    public Link() {
        super();
    }

    // --- Getters and Setters ---

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    // --- toString() method for debugging ---

    @Override
    public String toString() {
        return "Link [linkId=" + linkId + ", resumeId=" + resumeId + ", linkName=" + linkName + ", linkUrl=" + linkUrl
                + "]";
    }
}