package dev.commerce.dtos.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class NotificationSocketResponse {
    private UUID id;
    private String title;
    private String message;
    private boolean read;
    private String type;
    private String referenceId;
}

