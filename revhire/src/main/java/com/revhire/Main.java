package com.revhire;

import com.revhire.config.SQLInitializer;
import com.revhire.controller.AuthController;
import com.revhire.controller.DashboardController;
import com.revhire.model.User;

public class Main {

    public static void main(String[] args) {

        SQLInitializer.initializeDatabase();

        AuthController authController = new AuthController();
        DashboardController dashboardController = new DashboardController();

        while (true) {

            User user = authController.showAuthMenu();

            if (user == null) {
                System.out.println("Exiting RevHire. Goodbye!");
                break;
            }

            System.out.println("Welcome " + user.getName() +
                    " (" + user.getRole() + ")");

            dashboardController.showDashboard(user);
        }
    }
}
