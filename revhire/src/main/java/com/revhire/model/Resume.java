package com.revhire.model;

import java.util.Date;

public class Resume {

    private Long id;
    private Long userId;
    private String objective;
    private String education;
    private String experience;
    private String skills;
    private String projects;
    private Date updatedAt;

    public Resume() {}

    public Resume(Long userId, String objective,
                  String education, String experience,
                  String skills, String projects) {
        this.userId = userId;
        this.objective = objective;
        this.education = education;
        this.experience = experience;
        this.skills = skills;
        this.projects = projects;
    }

    // Generate getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
