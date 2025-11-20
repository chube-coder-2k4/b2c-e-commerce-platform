package dev.commerce.repositories.jpa;

import dev.commerce.entitys.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID>, JpaSpecificationExecutor<ProductImage> {
}
