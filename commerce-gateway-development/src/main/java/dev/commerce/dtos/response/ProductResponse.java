package dev.commerce.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ProductResponse {
    private UUID productId;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private UUID categoryId;
    private String categoryName;
    private boolean isActive;
    private UUID createdBy;
    private UUID updatedBy;
}
