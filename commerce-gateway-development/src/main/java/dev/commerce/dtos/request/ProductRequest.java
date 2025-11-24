package dev.commerce.dtos.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ProductRequest {
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private UUID categoryId;
}
