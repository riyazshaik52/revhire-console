package com.revhire.dao;

import com.revhire.config.DBConnection;
import com.revhire.model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    public boolean apply(Application app) {

        String sql = """
        INSERT INTO APPLICATIONS 
        (ID, JOB_ID, SEEKER_ID, RESUME_ID, COVER_LETTER)
        VALUES (APPLICATIONS_SEQ.NEXTVAL, ?, ?, ?, ?)
        """;


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, app.getJobId());
            stmt.setLong(2, app.getSeekerId());
            stmt.setLong(3, app.getResumeId());
            stmt.setString(4, app.getCoverLetter());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public List<Application> getApplicationsByJob(Long jobId) {

        List<Application> list = new ArrayList<>();

        String sql = """
        SELECT a.ID, a.STATUS, a.APPLIED_DATE,
               u.ID AS SEEKER_ID,
               u.NAME,
               r.EDUCATION,
               r.SKILLS,
               r.EXPERIENCE
        FROM APPLICATIONS a
        JOIN USERS u ON a.SEEKER_ID = u.ID
        JOIN RESUMES r ON a.RESUME_ID = r.ID
        WHERE a.JOB_ID = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, jobId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Application app = new Application();

                app.setId(rs.getLong("ID"));
                app.setSeekerId(rs.getLong("SEEKER_ID"));
                app.setStatus(rs.getString("STATUS"));

                app.setSeekerName(rs.getString("NAME"));
                app.setEducation(rs.getString("EDUCATION"));
                app.setSkills(rs.getString("SKILLS"));
                app.setExperience(rs.getString("EXPERIENCE"));

                list.add(app);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateStatus(Long appId, String status) {

        String sql = "UPDATE APPLICATIONS SET STATUS=? WHERE ID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setLong(2, appId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean withdrawApplication(Long appId, String reason) {

        String sql = """
        UPDATE APPLICATIONS
        SET STATUS='WITHDRAWN', WITHDRAWN_REASON=?
        WHERE ID=?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reason);
            stmt.setLong(2, appId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public List<Application> getBySeeker(Long seekerId) {

        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM APPLICATIONS WHERE SEEKER_ID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, seekerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getLong("ID"));
                app.setJobId(rs.getLong("JOB_ID"));
                app.setStatus(rs.getString("STATUS"));
                list.add(app);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Application getById(Long appId) {

        String sql = "SELECT * FROM APPLICATIONS WHERE ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, appId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Application app = new Application();
                app.setId(rs.getLong("ID"));
                app.setJobId(rs.getLong("JOB_ID"));
                app.setSeekerId(rs.getLong("SEEKER_ID"));
                app.setStatus(rs.getString("STATUS"));
                return app;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean exists(Long jobId, Long seekerId) {

        String sql = "SELECT COUNT(*) FROM APPLICATIONS WHERE JOB_ID=? AND SEEKER_ID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, jobId);
            stmt.setLong(2, seekerId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean alreadyApplied(Long jobId, Long seekerId) {

        String sql =
                "SELECT COUNT(*) FROM APPLICATIONS " +
                        "WHERE JOB_ID=? AND SEEKER_ID=? AND STATUS!='WITHDRAWN'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, jobId);
            stmt.setLong(2, seekerId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int countApplicationsByEmployer(Long employerId) {

        String sql = """
        SELECT COUNT(*)
        FROM APPLICATIONS a
        JOIN JOBS j ON a.JOB_ID = j.ID
        WHERE j.EMPLOYER_ID = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, employerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int countByStatus(Long employerId, String status) {

        String sql = """
        SELECT COUNT(*)
        FROM APPLICATIONS a
        JOIN JOBS j ON a.JOB_ID = j.ID
        WHERE j.EMPLOYER_ID = ? AND a.STATUS = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, employerId);
            stmt.setString(2, status);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

}
