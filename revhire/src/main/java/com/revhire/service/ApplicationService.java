package com.revhire.service;

import com.revhire.dao.ApplicationDAO;
import com.revhire.dao.NotificationDAO;
import com.revhire.model.Application;

import java.util.List;

public class ApplicationService {

    private final ApplicationDAO applicationDAO = new ApplicationDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    public boolean apply(Long jobId, Long seekerId, Long resumeId, String coverLetter, Long employerId) {

        if (applicationDAO.alreadyApplied(jobId, seekerId)) {
            System.out.println("You have already applied to this job.");
            return false;
        }

        Application app = new Application();
        app.setJobId(jobId);
        app.setSeekerId(seekerId);
        app.setResumeId(resumeId);
        app.setCoverLetter(coverLetter);

        boolean success = applicationDAO.apply(app);

        if (success) {
            notificationDAO.createNotification(
                    employerId,
                    "New application received for Job ID: " + jobId);
        }

        return success;
    }

    public List<Application> getApplicants(Long jobId) {
        return applicationDAO.getApplicationsByJob(jobId);
    }

    public boolean updateStatus(Long appId, String status, Long seekerId) {

        boolean success = applicationDAO.updateStatus(appId, status);

        if (success) {
            notificationDAO.createNotification(
                    seekerId,
                    "Your application status updated to: " + status);
        }

        return success;
    }

    public List<Application> getBySeeker(Long seekerId) {
        return applicationDAO.getBySeeker(seekerId);
    }

    public boolean withdraw(Long appId, String reason) {

        Application app = applicationDAO.getById(appId);

        if (app == null) {
            System.out.println("Application not found.");
            return false;
        }

        if (!app.getStatus().equals("APPLIED")) {
            System.out.println("Only APPLIED applications can be withdrawn.");
            return false;
        }

        return applicationDAO.withdrawApplication(appId, reason);
    }

    public Application getById(Long appId) {
        return applicationDAO.getById(appId);
    }





}
