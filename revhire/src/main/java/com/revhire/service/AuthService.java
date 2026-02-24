package com.revhire.service;

import com.revhire.dao.UserDAO;
import com.revhire.model.User;
import com.revhire.util.InputValidator;
import com.revhire.util.PasswordUtil;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public boolean register(String name,
                            String email,
                            String password,
                            String role,
                            String question,
                            String answer) {

        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return false;
        }

        if (!InputValidator.isValidPassword(password)) {
            System.out.println("Password must be at least 6 characters.");
            return false;
        }

        if (userDAO.findByEmail(email) != null) {
            System.out.println("Email already registered.");
            return false;
        }

        if (question == null || question.isBlank()
                || answer == null || answer.isBlank()) {
            System.out.println("Security question and answer required.");
            return false;
        }

        String hashedPassword = PasswordUtil.hashPassword(password);

        User user = new User(
                name,
                email,
                hashedPassword,
                role,
                question,
                answer
        );

        return userDAO.register(user);
    }

    public User login(String email, String password) {

        User user = userDAO.findByEmail(email);

        if (user == null) {
            System.out.println("User not found.");
            return null;
        }

        String hashedInput = PasswordUtil.hashPassword(password);

        if (!user.getPassword().equals(hashedInput)) {
            System.out.println("Incorrect password.");
            return null;
        }

        return user;
    }

    public boolean changePassword(Long userId, String newPassword) {

        if (!InputValidator.isValidPassword(newPassword)) {
            System.out.println("Password must be at least 6 characters.");
            return false;
        }

        String hashed = PasswordUtil.hashPassword(newPassword);

        return userDAO.updatePassword(userId, hashed);
    }

    public boolean changePassword(Long userId, String oldPass, String newPass) {

        User user = userDAO.findById(userId);

        if (user == null) {
            System.out.println("User not found.");
            return false;
        }

        String hashedOld = PasswordUtil.hashPassword(oldPass);

        if (!user.getPassword().equals(hashedOld)) {
            System.out.println("Current password incorrect.");
            return false;
        }

        if (!InputValidator.isValidPassword(newPass)) {
            System.out.println("Password must be at least 6 characters.");
            return false;
        }

        String hashedNew = PasswordUtil.hashPassword(newPass);

        return userDAO.updatePassword(userId, hashedNew);
    }

    public boolean forgotPassword(String email,
                                  String answer,
                                  String newPassword) {

        User user = userDAO.findByEmail(email);

        if (user == null) {
            System.out.println("User not found.");
            return false;
        }

        if (user.getSecurityAnswer() == null) {
            System.out.println("Security question not set for this account.");
            return false;
        }

        if (!user.getSecurityAnswer().equalsIgnoreCase(answer)) {
            System.out.println("Incorrect security answer.");
            return false;
        }

        if (!InputValidator.isValidPassword(newPassword)) {
            System.out.println("Password must be at least 6 characters.");
            return false;
        }

        String hashed = PasswordUtil.hashPassword(newPassword);

        return userDAO.updatePasswordByEmail(email, hashed);
    }

}
