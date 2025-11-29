package dev.commerce.repositories.jpa;

import dev.commerce.entitys.Cart;
import dev.commerce.entitys.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID>, JpaSpecificationExecutor<Cart> {
    Optional<Cart> findByUsers(Users users);
}
