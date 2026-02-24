package com.revhire.controller;

import com.revhire.model.User;
import com.revhire.service.AuthService;
import com.revhire.dao.UserDAO;
import com.revhire.util.InputValidator;
import com.revhire.util.PasswordUtil;

import java.util.Scanner;

public class AuthController {

    private final AuthService authService = new AuthService();
    private final Scanner scanner = new Scanner(System.in);

    public User showAuthMenu() {

        while (true) {
            System.out.println("\n=== RevHire Authentication ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("4. Exit");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> register();
                case 2 -> {
                    User user = login();
                    if (user != null) return user;
                }
                case 3 -> forgotPassword();
                case 4 -> System.exit(0);
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void register() {

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Role (JOB_SEEKER / EMPLOYER): ");
        String role = scanner.nextLine().toUpperCase();

        System.out.print("Security Question: ");
        String question = scanner.nextLine();

        System.out.print("Security Answer: ");
        String answer = scanner.nextLine();

        boolean success =
                authService.register(name, email, password, role, question, answer);

        if (success)
            System.out.println("Registration successful!");
        else
            System.out.println("Registration failed.");
    }

    private User login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = authService.login(email, password);

        if (user != null)
            System.out.println("Login successful!");

        return user;
    }

    public void changePassword(User user) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== Change Password ===");

        System.out.print("Enter Current Password: ");
        String oldPassword = scanner.nextLine();

        System.out.print("Enter New Password: ");
        String newPassword = scanner.nextLine();

        boolean success = authService.changePassword(
                user.getId(),
                oldPassword,
                newPassword
        );

        if (success) {
            System.out.println("Password updated successfully.");
        } else {
            System.out.println("Password update failed.");
        }
    }

    private void forgotPassword() {

        System.out.println("\n=== Forgot Password ===");

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Security Answer: ");
        String answer = scanner.nextLine();

        System.out.print("Enter New Password: ");
        String newPassword = scanner.nextLine();

        boolean success =
                authService.forgotPassword(email, answer, newPassword);

        if (success) {
            System.out.println("Password updated successfully.");
        } else {
            System.out.println("Password reset failed.");
        }
    }

}
