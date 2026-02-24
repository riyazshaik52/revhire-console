package com.revhire.dao;

import com.revhire.config.DBConnection;
import com.revhire.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public void createNotification(Long userId, String message) {

        String sql = "INSERT INTO NOTIFICATIONS (USER_ID, MESSAGE) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setString(2, message);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getUserNotifications(Long userId) {

        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM NOTIFICATIONS WHERE USER_ID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setId(rs.getLong("ID"));
                n.setMessage(rs.getString("MESSAGE"));
                n.setIsRead(rs.getInt("IS_READ"));
                list.add(n);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Notification> getByUser(Long userId) {

        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM NOTIFICATIONS WHERE USER_ID=? ORDER BY CREATED_AT DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setId(rs.getLong("ID"));
                n.setUserId(rs.getLong("USER_ID"));
                n.setMessage(rs.getString("MESSAGE"));
                n.setIsRead(rs.getInt("IS_READ"));
                list.add(n);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countUnread(Long userId) {

        String sql =
                "SELECT COUNT(*) FROM NOTIFICATIONS " +
                        "WHERE USER_ID=? AND IS_READ=0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean markAsRead(Long notificationId, Long userId) {

        String sql =
                "UPDATE NOTIFICATIONS SET IS_READ=1 " +
                        "WHERE ID=? AND USER_ID=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, notificationId);
            stmt.setLong(2, userId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
