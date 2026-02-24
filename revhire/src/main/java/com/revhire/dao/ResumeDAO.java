package com.revhire.dao;

import com.revhire.config.DBConnection;
import com.revhire.model.Resume;

import java.sql.*;

public class ResumeDAO {

    public boolean createResume(Resume resume) {

        String sql = "INSERT INTO RESUMES (USER_ID, OBJECTIVE, EDUCATION, EXPERIENCE, SKILLS, PROJECTS) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, resume.getUserId());
            stmt.setString(2, resume.getObjective());
            stmt.setString(3, resume.getEducation());
            stmt.setString(4, resume.getExperience());
            stmt.setString(5, resume.getSkills());
            stmt.setString(6, resume.getProjects());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Resume already exists for this user.");
            return false;
        }
    }

    public Resume getResumeByUserId(Long userId) {

        String sql = "SELECT * FROM RESUMES WHERE USER_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Resume resume = new Resume();
                resume.setId(rs.getLong("ID"));
                resume.setUserId(rs.getLong("USER_ID"));
                resume.setObjective(rs.getString("OBJECTIVE"));
                resume.setEducation(rs.getString("EDUCATION"));
                resume.setExperience(rs.getString("EXPERIENCE"));
                resume.setSkills(rs.getString("SKILLS"));
                resume.setProjects(rs.getString("PROJECTS"));
                return resume;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateResume(Resume resume) {

        String sql = "UPDATE RESUMES SET OBJECTIVE=?, EDUCATION=?, EXPERIENCE=?, SKILLS=?, PROJECTS=?, UPDATED_AT=SYSDATE WHERE USER_ID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, resume.getObjective());
            stmt.setString(2, resume.getEducation());
            stmt.setString(3, resume.getExperience());
            stmt.setString(4, resume.getSkills());
            stmt.setString(5, resume.getProjects());
            stmt.setLong(6, resume.getUserId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteResume(Long userId) {

        String sql = "DELETE FROM RESUMES WHERE USER_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public Resume getByUserId(Long userId) {

        String sql = "SELECT * FROM RESUMES WHERE USER_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Resume resume = new Resume();
                resume.setId(rs.getLong("ID"));
                resume.setUserId(rs.getLong("USER_ID"));
                resume.setObjective(rs.getString("OBJECTIVE"));
                resume.setEducation(rs.getString("EDUCATION"));
                resume.setExperience(rs.getString("EXPERIENCE"));
                resume.setSkills(rs.getString("SKILLS"));
                resume.setProjects(rs.getString("PROJECTS"));
                return resume;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
