package dev.commerce.mappers;

import dev.commerce.dtos.request.CategoryRequest;
import dev.commerce.dtos.response.CategoryResponse;
import dev.commerce.entitys.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category dtoToEntity(CategoryRequest categoryRequest);
    CategoryResponse entityToDto(Category category);
}
