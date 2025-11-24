package dev.commerce.mappers;

import dev.commerce.dtos.request.ProductRequest;
import dev.commerce.dtos.response.ProductResponse;
import dev.commerce.entitys.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductRequest request);
    ProductResponse toResponse(Product entity);
}
