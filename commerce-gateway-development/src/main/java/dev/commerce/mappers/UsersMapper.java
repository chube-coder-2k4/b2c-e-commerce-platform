package dev.commerce.mappers;

import dev.commerce.dtos.request.UserRequest;
import dev.commerce.dtos.response.UserResponse;
import dev.commerce.entitys.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provider", expression = "java(userRequest.getProvider() != null ? LoginType.valueOf(userRequest.getProvider()) : LoginType.LOCAL)")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Users toEntity(UserRequest dto);
    UserResponse toDto(Users entity);
}
