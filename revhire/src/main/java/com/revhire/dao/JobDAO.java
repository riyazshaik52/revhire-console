package com.revhire.dao;

import com.revhire.config.DBConnection;
import com.revhire.model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDAO {

    public boolean createJob(Job job) {

        String sql = """
                INSERT INTO JOBS 
                (EMPLOYER_ID, TITLE, DESCRIPTION, SKILLS, EXPERIENCE_YEARS,
                 EDUCATION, LOCATION, SALARY_MIN, SALARY_MAX, JOB_TYPE, DEADLINE)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, job.getEmployerId());
            stmt.setString(2, job.getTitle());
            stmt.setString(3, job.getDescription());
            stmt.setString(4, job.getSkills());
            stmt.setInt(5, job.getExperienceYears());
            stmt.setString(6, job.getEducation());
            stmt.setString(7, job.getLocation());
            stmt.setDouble(8, job.getSalaryMin());
            stmt.setDouble(9, job.getSalaryMax());
            stmt.setString(10, job.getJobType());
            stmt.setDate(11, new java.sql.Date(job.getDeadline().getTime()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Job> getJobsByEmployer(Long employerId) {

        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM JOBS WHERE EMPLOYER_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, employerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Job job = new Job();
                job.setId(rs.getLong("ID"));
                job.setTitle(rs.getString("TITLE"));
                job.setLocation(rs.getString("LOCATION"));
                job.setStatus(rs.getString("STATUS"));
                jobs.add(job);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobs;
    }

    public boolean updateJobStatus(Long jobId, String status) {

        String sql = "UPDATE JOBS SET STATUS=? WHERE ID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setLong(2, jobId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteJob(Long jobId) {

        String sql = "DELETE FROM JOBS WHERE ID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, jobId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public Job getJobById(Long jobId) {

        String sql = "SELECT * FROM JOBS WHERE ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, jobId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Job job = new Job();
                job.setId(rs.getLong("ID"));
                job.setEmployerId(rs.getLong("EMPLOYER_ID"));
                job.setTitle(rs.getString("TITLE"));
                job.setLocation(rs.getString("LOCATION"));
                job.setSkills(rs.getString("SKILLS"));
                return job;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Job> getAllOpenJobs() {

        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM JOBS WHERE STATUS='OPEN'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Job job = new Job();
                job.setId(rs.getLong("ID"));
                job.setTitle(rs.getString("TITLE"));
                job.setLocation(rs.getString("LOCATION"));
                job.setSkills(rs.getString("SKILLS"));
                jobs.add(job);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobs;
    }

    public List<Job> filterJobs(String location, String skills, Integer experience, Double salary, String jobType) {

        List<Job> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM JOBS WHERE STATUS='OPEN'"
        );

        if (location != null && !location.isBlank())
            sql.append(" AND LOWER(LOCATION) LIKE LOWER(?)");

        if (skills != null && !skills.isBlank())
            sql.append(" AND LOWER(SKILLS) LIKE LOWER(?)");

        if (experience != null)
            sql.append(" AND EXPERIENCE_YEARS >= ?");

        if (salary != null)
            sql.append(" AND SALARY_MIN >= ?");

        if (jobType != null && !jobType.isBlank())
            sql.append(" AND LOWER(JOB_TYPE) = LOWER(?)");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (location != null && !location.isBlank())
                stmt.setString(index++, "%" + location + "%");

            if (skills != null && !skills.isBlank())
                stmt.setString(index++, "%" + skills + "%");

            if (experience != null)
                stmt.setInt(index++, experience);

            if (salary != null)
                stmt.setDouble(index++, salary);

            if (jobType != null && !jobType.isBlank())
                stmt.setString(index++, jobType);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Job job = new Job();
                job.setId(rs.getLong("ID"));
                job.setTitle(rs.getString("TITLE"));
                job.setLocation(rs.getString("LOCATION"));
                job.setSalaryMin(rs.getDouble("SALARY_MIN"));
                job.setSalaryMax(rs.getDouble("SALARY_MAX"));
                job.setSkills(rs.getString("SKILLS"));
                list.add(job);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countJobsByEmployer(Long employerId) {

        String sql = "SELECT COUNT(*) FROM JOBS WHERE EMPLOYER_ID=?";

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



}
