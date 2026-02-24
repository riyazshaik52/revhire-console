package com.revhire.controller;

import com.revhire.model.Notification;
import com.revhire.model.User;
import com.revhire.service.NotificationService;

import java.util.List;
import java.util.Scanner;

public class NotificationController {

    private final NotificationService notificationService = new NotificationService();

    private final Scanner scanner = new Scanner(System.in);

    public void showNotifications(User user) {

        List<Notification> list =
                notificationService.getUserNotifications(user.getId());

        if (list.isEmpty()) {
            System.out.println("No notifications.");
            return;
        }

        System.out.println("\n=== Notifications ===");

        for (Notification n : list) {

            System.out.println(
                    "ID: " + n.getId()
                            + " | " + n.getMessage()
                            + " | Read: "
                            + (n.getIsRead() == 1 ? "YES" : "NO")
            );
        }

        System.out.println("\n1. Mark as Read");
        System.out.println("2. Back");

        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 1) {
            markAsRead(user);
        }
    }

    private void markAsRead(User user) {

        System.out.print("Enter Notification ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        boolean success =
                notificationService.markAsRead(id, user.getId());

        System.out.println(success ?
                "Marked as read." :
                "Failed.");
    }
}
