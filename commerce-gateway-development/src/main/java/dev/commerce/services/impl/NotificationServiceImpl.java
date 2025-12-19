package dev.commerce.services.impl;

import dev.commerce.dtos.response.NotificationSocketResponse;
import dev.commerce.dtos.websocket.OrderStatusMessage;
import dev.commerce.entitys.NotificationSocket;
import dev.commerce.entitys.Orders;
import dev.commerce.entitys.Payment;
import dev.commerce.entitys.Users;
import dev.commerce.exception.ResourceNotFoundException;
import dev.commerce.mappers.NotificationSocketMapper;
import dev.commerce.repositories.jpa.NotificationRepository;
import dev.commerce.services.NotificationService;
import dev.commerce.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final AuthenticationUtils utils;
    private final NotificationSocketMapper notificationSocketMapper;
    private final SimpMessagingTemplate messagingTemplate; //WebSocket messaging



    @Override
    public List<NotificationSocketResponse> getMyNotifications() {
        Users user = utils.getCurrentUser();
        List<NotificationSocket> notificationSocket = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        return notificationSocket.stream().map(notificationSocketMapper::mapToResponse).toList();
    }

    @Override
    public void markAsRead(UUID notificationIds) {
        Users user = utils.getCurrentUser();
        NotificationSocket notification =
                notificationRepository.findByIdAndUser(notificationIds, user)
                        .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        if(!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    @Transactional
    @Override
    public void markAsReadAllNotifications() {
        Users user = utils.getCurrentUser();
        List<NotificationSocket> notifications = notificationRepository.findByUserAndReadIsFalseOrderByCreatedAtDesc(user);
        notifications.forEach(notification -> notification.setRead(true));
    }

    @Override
    public void deleteNotifications(UUID notificationIds) {
        Users user = utils.getCurrentUser();
        NotificationSocket notification =
                notificationRepository.findByIdAndUser(notificationIds, user)
                        .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notificationRepository.delete(notification);
    }

    @Override
    public void deleteAllNotifications() {
        Users user = utils.getCurrentUser();
        notificationRepository.deleteByUser(user);
    }

    @Transactional
    @Override
    public void notifyOrderStatus(Orders order) {
        Users user = order.getUsers();
        NotificationSocket notification = new NotificationSocket();
        notification.setUser(user);
        notification.setTitle("Order Status Updated");
        notification.setMessage("Your order " + order.getOrderCode() + " status has been updated to " + order.getStatus());
        notification.setType("ORDER_STATUS");
        notification.setReferenceId(order.getId().toString());
        notification.setRead(false);
        notificationRepository.save(notification);

        OrderStatusMessage payload = new OrderStatusMessage(
                order.getId().toString(),
                order.getStatus().name(),
                "Your order " + order.getOrderCode() + " is now " + order.getStatus()
        );

        messagingTemplate.convertAndSend("/topic/order-status." + user.getId(), payload);
    }

    @Override
    public void notifyPaymentStatus(Payment payment) {
        Users user = payment.getOrders().getUsers();
        NotificationSocket notification = new NotificationSocket();
        notification.setUser(user);
        notification.setTitle("Payment Status Updated");
        notification.setMessage("Your payment for order " + payment.getOrders().getOrderCode() + " has been " + payment.getStatus());
        notification.setType("PAYMENT_STATUS");
        notification.setReferenceId(payment.getId().toString());
        notification.setRead(false);
        notificationRepository.save(notification);

        messagingTemplate.convertAndSend(
                "/topic/payment-status." + user.getId(),
                notificationSocketMapper.mapToResponse(notification)
        );
    }

    @Override
    public void notifyGeneral(String title, String message, String type, String referenceId) {
        Users user = utils.getCurrentUser();
        NotificationSocket notification = new NotificationSocket();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setReferenceId(referenceId);
        notification.setRead(false);
        notificationRepository.save(notification);
    }
}
