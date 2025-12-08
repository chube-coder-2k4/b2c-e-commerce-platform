package dev.commerce.repositories.jpa;

import dev.commerce.entitys.Orders;
import dev.commerce.entitys.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID>, JpaSpecificationExecutor<OrderItem> {
    //find By Orders
    List<OrderItem> findByOrders(Orders orders);
}
