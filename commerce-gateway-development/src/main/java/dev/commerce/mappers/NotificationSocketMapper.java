package dev.commerce.mappers;

import dev.commerce.dtos.response.NotificationSocketResponse;
import dev.commerce.entitys.NotificationSocket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationSocketMapper {
    NotificationSocketResponse mapToResponse(NotificationSocket notificationSocket);
}
