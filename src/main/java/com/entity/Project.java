package com.entity;

/**
 * Represents a single project entry in a resume.
 * This class is a POJO (Plain Old Java Object) to hold project data.
 */
public class Project {

    private int projectId;
    private int resumeId;
    private String projectTitle;
    private String projectLink;
    private String description;

    /**
     * Default constructor.
     */
    public Project() {
        super();
    }

    // --- Getters and Setters ---

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // --- toString() method for debugging ---

    @Override
    public String toString() {
        return "Project [projectId=" + projectId + ", resumeId=" + resumeId + ", projectTitle=" + projectTitle
                + ", projectLink=" + projectLink + ", description=" + description + "]";
    }
}