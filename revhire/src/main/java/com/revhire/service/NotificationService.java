package com.revhire.service;

import com.revhire.dao.NotificationDAO;
import com.revhire.model.Notification;

import java.util.List;

public class NotificationService {


    private final NotificationDAO notificationDAO = new NotificationDAO();

    public List<Notification> getUserNotifications(Long userId) {
        return notificationDAO.getByUser(userId);
    }

    public boolean markAsRead(Long notificationId, Long userId) {
        return notificationDAO.markAsRead(notificationId, userId);
    }

    public void createNotification(Long userId, String message) {
        notificationDAO.createNotification(userId, message);
    }

    public int getUnreadCount(Long userId) {
        return notificationDAO.countUnread(userId);
    }

}
