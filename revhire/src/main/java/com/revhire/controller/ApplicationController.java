package com.revhire.controller;

import com.revhire.model.Application;
import com.revhire.model.Job;
import com.revhire.model.Resume;
import com.revhire.model.User;
import com.revhire.service.ApplicationService;
import com.revhire.service.JobService;
import com.revhire.service.ResumeService;

import java.util.List;
import java.util.Scanner;


public class ApplicationController {

    private final ResumeService resumeService = new ResumeService();
    private final JobService jobService = new JobService();
    private final ApplicationService applicationService = new ApplicationService();
    private final Scanner scanner = new Scanner(System.in);

    public void showSeekerMenu(User seeker) {

        while (true) {
            System.out.println("\n=== Job Seeker Dashboard ===");
            System.out.println("1. View All Jobs");
            System.out.println("2. Filter Jobs");
            System.out.println("3. Apply to Job");
            System.out.println("4. Back");


            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> searchJobs();
                case 2 -> filterJobs();
                case 3 -> apply(seeker);
                case 4 -> { return; }
            }
        }
    }

    private void searchJobs() {
        List<Job> jobs = jobService.getAllOpenJobs();

        for (Job job : jobs) {
            System.out.println("ID: " + job.getId()
                    + " | " + job.getTitle()
                    + " | " + job.getLocation()
                    + " | Skills: " + job.getSkills());
        }
    }

    private void apply(User seeker) {

        System.out.print("Enter Job ID: ");
        Long jobId = Long.parseLong(scanner.nextLine());

        Job job = jobService.getJobById(jobId);

        if (job.getDeadline() != null &&
                job.getDeadline().before(new java.util.Date())) {
            System.out.println("Application deadline has passed.");
            return;
        }


        System.out.println("\n=== Job Details ===");
        System.out.println("Title: " + job.getTitle());
        System.out.println("Location: " + job.getLocation());
        System.out.println("Skills: " + job.getSkills());

        System.out.print("\nDo you want to apply? (Y/N): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {
            return;
        }

        Resume resume = resumeService.getResumeByUser(seeker.getId());

        if (resume == null) {
            System.out.println("You must create a resume before applying.");
            return;
        }

        Long resumeId = resume.getId();


        System.out.print("Do you want to add a cover letter? (Y/N): ");
        String coverChoice = scanner.nextLine();

        String cover = null;

        if (coverChoice.equalsIgnoreCase("Y")) {
            System.out.print("Enter Cover Letter: ");
            cover = scanner.nextLine();
        }

        Long employerId = job.getEmployerId();

        boolean success = applicationService.apply(
                jobId,
                seeker.getId(),
                resumeId,
                cover,
                employerId
        );

        System.out.println(success ? "Applied successfully." : "Failed.");
    }

    public void showMyApplications(User seeker) {

        while (true) {

            System.out.println("\n=== My Applications ===");

            List<Application> apps =
                    applicationService.getBySeeker(seeker.getId());

            if (apps.isEmpty()) {
                System.out.println("No applications found.");
            } else {
                for (Application app : apps) {
                    System.out.println("Application ID: " + app.getId());
                    System.out.println("Job ID: " + app.getJobId());
                    System.out.println("Status: " + app.getStatus());
                    System.out.println("---------------------------");
                }

            }

            System.out.println("\n1. Withdraw Application");
            System.out.println("2. Back");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> withdrawApplication(seeker);
                case 2 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void withdrawApplication(User seeker) {

        System.out.print("Enter Application ID to withdraw: ");
        Long appId = Long.parseLong(scanner.nextLine());

        Application app = applicationService.getById(appId);

        if (app == null) {
            System.out.println("Application not found.");
            return;
        }

        if (!app.getSeekerId().equals(seeker.getId())) {
            System.out.println("You cannot withdraw someone else's application.");
            return;
        }

        System.out.print("Reason: ");
        String reason = scanner.nextLine();

        boolean success = applicationService.withdraw(appId, reason);

        System.out.println(success ? "Application withdrawn."
                : "Failed.");
    }

    private void filterJobs() {

        System.out.print("Location (leave blank to skip): ");
        String location = scanner.nextLine();

        System.out.print("Skills (leave blank to skip): ");
        String skills = scanner.nextLine();

        System.out.print("Minimum Experience (leave blank to skip): ");
        String expInput = scanner.nextLine();
        Integer experience = expInput.isBlank() ? null : Integer.parseInt(expInput);

        System.out.print("Minimum Salary (leave blank to skip): ");
        String salaryInput = scanner.nextLine();
        Double salary = salaryInput.isBlank() ? null : Double.parseDouble(salaryInput);

        System.out.print("Job Type (leave blank to skip): ");
        String jobType = scanner.nextLine();

        List<Job> jobs = jobService.filterJobs(
                location,
                skills,
                experience,
                salary,
                jobType
        );

        if (jobs.isEmpty()) {
            System.out.println("No matching jobs found.");
            return;
        }

        System.out.println("\n=== Filtered Jobs ===");

        for (Job job : jobs) {
            System.out.println("ID: " + job.getId()
                    + " | " + job.getTitle()
                    + " | " + job.getLocation()
                    + " | Salary: " + job.getSalaryMin()
                    + " - " + job.getSalaryMax());
        }
    }




}
