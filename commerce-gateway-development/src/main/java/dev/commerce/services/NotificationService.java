package dev.commerce.services;

import dev.commerce.dtos.response.NotificationSocketResponse;
import dev.commerce.entitys.Orders;
import dev.commerce.entitys.Payment;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationSocketResponse> getMyNotifications();
    void markAsRead(UUID notificationIds);
    void markAsReadAllNotifications();
    void deleteNotifications(UUID notificationIds);
    void deleteAllNotifications();
    void notifyOrderStatus(Orders order);
    void notifyPaymentStatus(Payment payment);
    void notifyGeneral(String title, String message, String type, String referenceId);
}
