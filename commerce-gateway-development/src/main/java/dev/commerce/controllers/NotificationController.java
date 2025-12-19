package dev.commerce.controllers;

import dev.commerce.dtos.response.NotificationSocketResponse;
import dev.commerce.services.NotificationService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@Validated
@Slf4j
@Tag(name = "Notification Controller", description = "APIs for managing notification of user")
public class NotificationController {
    private final NotificationService notificationService;



    @GetMapping("/my-notifications")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse (responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<List<NotificationSocketResponse>> getMyNotifications() {
        log.info("Retrieving current user's notifications" );
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }

    // đánh dấu là đã đọc tất cả thông báo nên để get hay post nhỉ
    //   @GetMapping("/mark-as-read-all")
    @GetMapping("/mark-as-read-all")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "All notifications marked as read successfully"),
            @ApiResponse (responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<String> markAsReadAllNotifications() {
        log.info("Marking all notifications as read for current user" );
        notificationService.markAsReadAllNotifications();
        return ResponseEntity.ok("All notifications marked as read successfully");
    }

    //    void deleteNotifications(UUID notificationIds);
    //    void deleteAllNotifications();
    @GetMapping("/mark-as-read")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Notification marked as read successfully"),
            @ApiResponse (responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<String> markAsReadNotifications(@RequestParam UUID notificationIds) {
        log.info("Marking notification as read for current user");
        notificationService.markAsRead(notificationIds);
        return ResponseEntity.ok("Notification marked as read successfully");
    }

    @DeleteMapping("/all-notifications")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "All notifications deleted successfully"),
            @ApiResponse (responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<String> deleteAllNotifications() {
        log.info("Deleting all notifications for current user");
        notificationService.deleteAllNotifications();
        return ResponseEntity.ok("All notifications deleted successfully");
    }

    @DeleteMapping("/notification")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Notification deleted successfully"),
            @ApiResponse (responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<String> deleteNotifications(@RequestParam UUID notificationIds) {
        log.info("Deleting notification for current user");
        notificationService.deleteNotifications(notificationIds);
        return ResponseEntity.ok("Notification deleted successfully");
    }


}
