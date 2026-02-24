package com.revhire.controller;

import com.revhire.model.Resume;
import com.revhire.model.User;
import com.revhire.service.ResumeService;

import java.util.Scanner;

public class ResumeController {

    private final ResumeService resumeService = new ResumeService();
    private final Scanner scanner = new Scanner(System.in);

    public void showResumeMenu(User user) {

        if (!user.getRole().equals("JOB_SEEKER")) {
            System.out.println("Only Job Seekers can manage resumes.");
            return;
        }

        while (true) {
            System.out.println("\n=== Resume Menu ===");
            System.out.println("1. Create Resume");
            System.out.println("2. View Resume");
            System.out.println("3. Update Resume");
            System.out.println("4. Delete Resume");
            System.out.println("5. Back");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> create(user);
                case 2 -> view(user);
                case 3 -> update(user);
                case 4 -> delete(user);
                case 5 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void create(User user) {
        System.out.print("Objective: ");
        String objective = scanner.nextLine();

        System.out.print("Education: ");
        String education = scanner.nextLine();

        System.out.print("Experience: ");
        String experience = scanner.nextLine();

        System.out.print("Skills: ");
        String skills = scanner.nextLine();

        System.out.print("Projects: ");
        String projects = scanner.nextLine();

        boolean success = resumeService.createResume(
                user.getId(), objective, education,
                experience, skills, projects);

        System.out.println(success ? "Resume created." : "Failed.");
    }

    private void view(User user) {
        Resume resume = resumeService.viewResume(user.getId());

        if (resume == null) {
            System.out.println("No resume found.");
            return;
        }

        System.out.println("\n--- Resume ---");
        System.out.println("Objective: " + resume.getObjective());
        System.out.println("Education: " + resume.getEducation());
        System.out.println("Experience: " + resume.getExperience());
        System.out.println("Skills: " + resume.getSkills());
        System.out.println("Projects: " + resume.getProjects());
    }

    private void update(User user) {
        create(user); // reuse same input logic
    }

    private void delete(User user) {
        boolean success = resumeService.deleteResume(user.getId());
        System.out.println(success ? "Deleted." : "Failed.");
    }
}
