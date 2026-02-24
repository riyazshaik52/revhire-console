package com.revhire.controller;

import com.revhire.model.User;
import com.revhire.controller.AuthController;
import com.revhire.service.NotificationService;

import java.util.Scanner;

public class DashboardController {

    private final Scanner scanner = new Scanner(System.in);

    private final ResumeController resumeController = new ResumeController();
    private final ApplicationController applicationController = new ApplicationController();
    private final JobController jobController = new JobController();
    private final NotificationController notificationController = new NotificationController();
    private final AuthController authController = new AuthController();
    private final NotificationService notificationService = new NotificationService();


    public void showDashboard(User user) {

        if (user.getRole().equals("JOB_SEEKER")) {
            showSeekerDashboard(user);
        } else {
            showEmployerDashboard(user);
        }
    }

    private void showSeekerDashboard(User user) {

        while (true) {

            int unread = notificationService.getUnreadCount(user.getId());
            System.out.println("\n=== Job Seeker Dashboard (🔔 " + unread + " Unread) ===");

            System.out.println("1. Resume Management");
            System.out.println("2. Job Search & Apply");
            System.out.println("3. My Applications");
            System.out.println("4. View Notifications");
            System.out.println("5. Change Password");
            System.out.println("6. Logout");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> resumeController.showResumeMenu(user);
                case 2 -> applicationController.showSeekerMenu(user);
                case 3 -> applicationController.showMyApplications(user);
                case 4 -> notificationController.showNotifications(user);
                case 5 -> authController.changePassword(user);
                case 6-> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void showEmployerDashboard(User user) {

        while (true) {

            int unread = notificationService.getUnreadCount(user.getId());
            System.out.println("\n=== Employer Dashboard (🔔 " + unread + " Unread) ===");

            System.out.println("1. Manage Jobs");
            System.out.println("2. View Notifications");
            System.out.println("3. Logout");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> jobController.showEmployerMenu(user);
                case 2 -> notificationController.showNotifications(user);
                case 3 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
