package dev.commerce.controllers;

import dev.commerce.dtos.common.OrderStatus;
import dev.commerce.dtos.request.OrderRequest;
import dev.commerce.dtos.response.OrderDetailResponse;
import dev.commerce.dtos.response.OrderResponse;
import dev.commerce.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/order")
@Validated
@Slf4j
@Tag(name = "Orders Controller", description = "APIs for managing orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create a new orders", description = "Creates a new orders for the authenticated user based on their cart contents.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid orders request"),
            @ApiResponse(responseCode = "404", description = "Cart not found or is empty")
    })
    @PostMapping()
    public ResponseEntity<OrderDetailResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderDetailResponse response = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get orders for the authenticated user", description = "Retrieves all orders placed by the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/user")
    public ResponseEntity<List<OrderDetailResponse>> getUserOrders() {
        return ResponseEntity.ok(orderService.getUserOrders());
    }


    @Operation(summary = "Get all orders", description = "Retrieves all orders in the system. Admin access required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access")
    })
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Operation(summary = "Get orders details", description = "Retrieves detailed information about a specific orders by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Orders not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetails(@PathVariable("orderId") UUID orderId) {
        OrderDetailResponse response = orderService.getOrderDetails(orderId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update orders status", description = "Updates the status of a specific orders. Admin access required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Orders not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden access")
    })
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable("orderId") UUID orderId,
                                               @RequestParam("status") OrderStatus status) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, status));
    }

    @Operation(summary = "Cancel an orders", description = "Cancels a specific orders by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Orders not found")
    })
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable("orderId") UUID orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }


}
