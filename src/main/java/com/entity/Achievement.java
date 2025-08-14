package com.entity;

/**
 * Represents a single achievement or award entry in a resume.
 * This class is a POJO (Plain Old Java Object) to hold achievement data.
 */
public class Achievement {

    private int achievementId;
    private int resumeId;
    private String description;

    /**
     * Default constructor.
     */
    public Achievement() {
        super();
    }

    // --- Getters and Setters ---

    public int getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(int achievementId) {
        this.achievementId = achievementId;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
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
        return "Achievement [achievementId=" + achievementId + ", resumeId=" + resumeId + ", description=" + description
                + "]";
    }
}