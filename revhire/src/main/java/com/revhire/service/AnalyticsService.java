package com.revhire.service;

import com.revhire.dao.ApplicationDAO;
import com.revhire.dao.JobDAO;

public class AnalyticsService {

    private final ApplicationDAO applicationDAO = new ApplicationDAO();
    private final JobDAO jobDAO = new JobDAO();

    public void showEmployerAnalytics(Long employerId) {

        int totalJobs = jobDAO.countJobsByEmployer(employerId);
        int totalApps =
                applicationDAO.countApplicationsByEmployer(employerId);

        int shortlisted =
                applicationDAO.countByStatus(employerId, "SHORTLISTED");

        int rejected =
                applicationDAO.countByStatus(employerId, "REJECTED");

        int pending =
                applicationDAO.countByStatus(employerId, "APPLIED");

        System.out.println("\n=== Recruiter Analytics ===");
        System.out.println("Total Jobs Posted: " + totalJobs);
        System.out.println("Total Applications: " + totalApps);
        System.out.println("Shortlisted: " + shortlisted);
        System.out.println("Rejected: " + rejected);
        System.out.println("Pending: " + pending);
    }
}
