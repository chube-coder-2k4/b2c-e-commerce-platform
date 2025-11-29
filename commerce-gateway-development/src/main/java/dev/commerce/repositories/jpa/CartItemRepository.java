package dev.commerce.repositories.jpa;

import dev.commerce.entitys.Cart;
import dev.commerce.entitys.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID>, JpaSpecificationExecutor<CartItem> {
    Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);

    @Query("SELECT SUM(ci.quantity * ci.product.price) FROM CartItem ci WHERE ci.cart.id = :cartId")
    Double sumTotalPrice(UUID cartId);

    void deleteByCartId(UUID cartId);

    List<CartItem> findByCart(Cart cart);
}
