package dev.commerce.mappers;

import dev.commerce.dtos.response.OrderDetailResponse;
import dev.commerce.dtos.response.OrderItemResponse;
import dev.commerce.dtos.response.OrderResponse;
import dev.commerce.entitys.Orders;
import dev.commerce.entitys.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // OrderItem -> OrderItemResponse
    default OrderItemResponse toOrderItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getUnitPrice() * item.getQuantity()
        );
    }

    // List<OrderItem> -> List<OrderItemResponse>
    default List<OrderItemResponse> toOrderItemResponses(List<OrderItem> items) {
        return items.stream()
                .map(this::toOrderItemResponse)
                .toList();
    }

    // Orders -> OrderResponse
    default OrderResponse toOrderResponse(Orders orders) {
        return OrderResponse.builder()
                .id(orders.getId())
                .orderCode(orders.getOrderCode())
                .totalAmount(orders.getTotalAmount())
                .status(orders.getStatus().name())
                .usersId(orders.getUsers().getId())
                .createdAt(orders.getCreatedAt())
                .updatedAt(orders.getUpdatedAt())
                .build();
    }

    // Orders + List<OrderItem> -> OrderDetailResponse (detailed)
    default OrderDetailResponse toOrderDetailResponse(Orders orders, List<OrderItem> items) {
        return new OrderDetailResponse(
                orders.getId(),
                orders.getOrderCode(),
                orders.getTotalAmount(),
                orders.getStatus(),
                orders.getPaymentMethod(),
                orders.getShippingAddress(),
                orders.getCreatedAt(),
                toOrderItemResponses(items),
                orders.getUsers().getId(),
                orders.getCreatedBy(),
                orders.getUpdatedBy()
        );
    }
}
