package com.revhire.controller;

import com.revhire.model.Application;
import com.revhire.model.Resume;
import com.revhire.service.AnalyticsService;
import com.revhire.service.ApplicationService;
import java.util.List;
import com.revhire.model.Job;
import com.revhire.model.User;
import com.revhire.service.JobService;
import com.revhire.service.ResumeService;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class JobController {

    private final JobService jobService = new JobService();
    private final Scanner scanner = new Scanner(System.in);
    private final ApplicationService applicationService = new ApplicationService();
    private final ResumeService resumeService = new ResumeService();

    public void showEmployerMenu(User employer) {

        if (!employer.getRole().equals("EMPLOYER")) {
            System.out.println("Only Employers allowed.");
            return;
        }

        while (true) {
            System.out.println("\n=== Employer Dashboard ===");
            System.out.println("1. Post Job");
            System.out.println("2. View My Jobs");
            System.out.println("3. View Applicants");
            System.out.println("4. Close Job");
            System.out.println("5. Reopen Job");
            System.out.println("6. Delete Job");
            System.out.println("7. View Analytics");
            System.out.println("8. Back");


            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> postJob(employer);
                case 2 -> viewJobs(employer);
                case 3 -> viewApplicants();
                case 4 -> changeStatus("CLOSE");
                case 5 -> changeStatus("OPEN");
                case 6 -> deleteJob();
                case 7 -> showAnalytics(employer);
                case 8 -> { return; }
                default -> System.out.println("Invalid choice.");
            }

        }
    }

    private void postJob(User employer) {

        try {
            System.out.print("Title: ");
            String title = scanner.nextLine();

            System.out.print("Description: ");
            String description = scanner.nextLine();

            System.out.print("Skills: ");
            String skills = scanner.nextLine();

            System.out.print("Experience Years: ");
            int experience = Integer.parseInt(scanner.nextLine());

            System.out.print("Education: ");
            String education = scanner.nextLine();

            System.out.print("Location: ");
            String location = scanner.nextLine();

            System.out.print("Min Salary: ");
            double minSalary = Double.parseDouble(scanner.nextLine());

            System.out.print("Max Salary: ");
            double maxSalary = Double.parseDouble(scanner.nextLine());

            System.out.print("Job Type: ");
            String jobType = scanner.nextLine();

            System.out.print("Deadline (yyyy-MM-dd): ");
            String dateStr = scanner.nextLine();
            Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

            boolean success = jobService.postJob(
                    employer.getId(), title, description,
                    skills, experience, education,
                    location, minSalary, maxSalary,
                    jobType, deadline);

            System.out.println(success ? "Job posted." : "Failed.");

        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void viewJobs(User employer) {
        List<Job> jobs = jobService.viewEmployerJobs(employer.getId());

        if (jobs.isEmpty()) {
            System.out.println("No jobs posted.");
            return;
        }

        for (Job job : jobs) {
            System.out.println("ID: " + job.getId()
                    + " | Title: " + job.getTitle()
                    + " | Location: " + job.getLocation()
                    + " | Status: " + job.getStatus());
        }
    }

    private void changeStatus(String type) {
        System.out.print("Enter Job ID: ");
        Long jobId = Long.parseLong(scanner.nextLine());

        boolean success = type.equals("CLOSE")
                ? jobService.closeJob(jobId)
                : jobService.reopenJob(jobId);

        System.out.println(success ? "Updated." : "Failed.");
    }

    private void deleteJob() {
        System.out.print("Enter Job ID: ");
        Long jobId = Long.parseLong(scanner.nextLine());

        boolean success = jobService.deleteJob(jobId);
        System.out.println(success ? "Deleted." : "Failed.");
    }

    private void viewApplicants() {

        System.out.print("Enter Job ID: ");
        Long jobId = Long.parseLong(scanner.nextLine());

        List<Application> apps =
                applicationService.getApplicants(jobId);

        if (apps.isEmpty()) {
            System.out.println("No applicants found.");
            return;
        }

        System.out.println("\n=== Applicants ===");

        for (Application app : apps) {

            System.out.println("\n---------------------------");
            System.out.println("App ID: " + app.getId());
            System.out.println("Name: " + app.getSeekerName());
            System.out.println("Education: " + app.getEducation());
            System.out.println("Skills: " + app.getSkills());
            System.out.println("Experience: " + app.getExperience());
            System.out.println("Status: " + app.getStatus());
        }

        manageApplicant();
    }

    private void manageApplicant() {

        System.out.println("\n1. View Resume");
        System.out.println("2. Shortlist");
        System.out.println("3. Reject");
        System.out.println("4. Back");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> viewResume();
            case 2 -> updateApplicationStatus("SHORTLISTED");
            case 3 -> updateApplicationStatus("REJECTED");
            case 4 -> { return; }
            default -> System.out.println("Invalid choice.");
        }
    }

    private void updateApplicationStatus(String status) {

        System.out.print("Enter Application IDs (comma separated): ");
        String input = scanner.nextLine();

        String[] ids = input.split(",");

        int successCount = 0;

        for (String idStr : ids) {

            try {

                Long appId = Long.parseLong(idStr.trim());

                Application app =
                        applicationService.getById(appId);

                if (app == null) {
                    System.out.println("Application ID "
                            + appId + " not found.");
                    continue;
                }

                boolean success =
                        applicationService.updateStatus(
                                appId,
                                status,
                                app.getSeekerId()
                        );

                if (success) successCount++;

            } catch (Exception e) {
                System.out.println("Invalid ID format.");
            }
        }

        System.out.println(successCount
                + " application(s) updated to "
                + status);
    }

    private void viewResume() {

        System.out.print("Enter Application ID: ");
        Long appId = Long.parseLong(scanner.nextLine());

        Application app = applicationService.getById(appId);

        if (app == null) {
            System.out.println("Application not found.");
            return;
        }

        Resume resume =
                resumeService.getResumeByUser(app.getSeekerId());

        if (resume == null) {
            System.out.println("Resume not found.");
            return;
        }

        System.out.println("\n=== Resume ===");
        System.out.println("Objective: " + resume.getObjective());
        System.out.println("Education: " + resume.getEducation());
        System.out.println("Experience: " + resume.getExperience());
        System.out.println("Skills: " + resume.getSkills());
        System.out.println("Projects: " + resume.getProjects());
    }

    private void showAnalytics(User employer) {

        AnalyticsService analyticsService = new AnalyticsService();

        analyticsService.showEmployerAnalytics(
                employer.getId());
    }

}
