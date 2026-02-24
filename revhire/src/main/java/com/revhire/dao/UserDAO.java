package com.revhire.dao;

import com.revhire.config.DBConnection;
import com.revhire.model.User;

import java.sql.*;

public class UserDAO {

    public boolean register(User user) {

        String sql = "INSERT INTO USERS " +
                "(ID, NAME, EMAIL, PASSWORD, ROLE, SECURITY_QUESTION, SECURITY_ANSWER) " +
                "VALUES (USERS_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getSecurityQuestion());
            stmt.setString(6, user.getSecurityAnswer());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM USERS WHERE EMAIL = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("ID"));
                user.setName(rs.getString("NAME"));
                user.setEmail(rs.getString("EMAIL"));
                user.setPassword(rs.getString("PASSWORD"));
                user.setRole(rs.getString("ROLE"));
                user.setSecurityQuestion(rs.getString("SECURITY_QUESTION"));
                user.setSecurityAnswer(rs.getString("SECURITY_ANSWER"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updatePassword(Long userId, String password) {

        String sql = "UPDATE USERS SET PASSWORD=? WHERE ID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, password);
            stmt.setLong(2, userId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public User findById(Long id) {

        String sql = "SELECT * FROM USERS WHERE ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("ID"));
                user.setName(rs.getString("NAME"));
                user.setEmail(rs.getString("EMAIL"));
                user.setPassword(rs.getString("PASSWORD"));
                user.setRole(rs.getString("ROLE"));
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updatePasswordByEmail(String email, String newPassword) {

        String sql = "UPDATE USERS SET PASSWORD=? WHERE EMAIL=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, email);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
