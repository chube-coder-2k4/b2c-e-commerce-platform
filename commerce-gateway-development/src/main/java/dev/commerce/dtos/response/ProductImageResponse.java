package dev.commerce.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
public class ProductImageResponse {
    private UUID id;
    private String imageUrl;
    private UUID productId;
    private String publicId;
    private boolean isPrimary;
    private UUID createdBy;
    private UUID updatedBy;
}
