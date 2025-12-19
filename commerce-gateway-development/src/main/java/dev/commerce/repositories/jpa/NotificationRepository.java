package dev.commerce.repositories.jpa;

import dev.commerce.entitys.NotificationSocket;
import dev.commerce.entitys.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationSocket, UUID> {

    List<NotificationSocket> findByUserOrderByCreatedAtDesc(Users user);
    List<NotificationSocket> findByUserAndReadIsFalseOrderByCreatedAtDesc(Users user);
    void deleteByUser(Users user);
    Optional<NotificationSocket> findByIdAndUser(UUID id, Users user);
}
