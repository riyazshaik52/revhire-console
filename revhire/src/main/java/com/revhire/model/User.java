package com.revhire.model;

import java.util.Date;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String role; // JOB_SEEKER / EMPLOYER
    private String phone;
    private String securityQuestion;
    private String securityAnswer;
    private int profileComplete;
    private Date createdAt;

    public User() {}

    public User(String name,
                String email,
                String password,
                String role,
                String securityQuestion,
                String securityAnswer) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public int getProfileComplete() {
        return profileComplete;
    }

    public void setProfileComplete(int profileComplete) {
        this.profileComplete = profileComplete;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
// Getters and Setters
}
