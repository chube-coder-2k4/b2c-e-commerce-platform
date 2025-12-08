package dev.commerce.repositories.jpa;

import dev.commerce.entitys.Orders;
import dev.commerce.entitys.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Orders, UUID>, JpaSpecificationExecutor<Orders> {
    List<Orders> findByUsers(Users users);
}
