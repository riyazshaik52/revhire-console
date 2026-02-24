package com.revhire.service;

import com.revhire.dao.JobDAO;
import com.revhire.model.Job;

import java.util.Date;
import java.util.List;

public class JobService {

    private final JobDAO jobDAO = new JobDAO();

    public boolean postJob(Long employerId, String title, String description, String skills, int experience, String education, String location, double minSalary, double maxSalary, String jobType, Date deadline) {

        Job job = new Job(employerId, title, description, skills,
                experience, education, location,
                minSalary, maxSalary, jobType, deadline);

        return jobDAO.createJob(job);
    }

    public List<Job> viewEmployerJobs(Long employerId) {
        return jobDAO.getJobsByEmployer(employerId);
    }

    public boolean closeJob(Long jobId) {
        return jobDAO.updateJobStatus(jobId, "CLOSED");
    }

    public boolean reopenJob(Long jobId) {
        return jobDAO.updateJobStatus(jobId, "OPEN");
    }

    public boolean deleteJob(Long jobId) {
        return jobDAO.deleteJob(jobId);
    }

    public List<Job> getAllOpenJobs() {
        return jobDAO.getAllOpenJobs();
    }

    public Job getJobById(Long jobId) {
        return jobDAO.getJobById(jobId);
    }

    public List<Job> filterJobs(String location, String skills, Integer experience, Double salary, String jobType) {

        return jobDAO.filterJobs(location, skills, experience, salary, jobType);
    }



}
