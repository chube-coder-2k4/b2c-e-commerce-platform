package dev.commerce.repositories.jpa;

import dev.commerce.entitys.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID>, JpaSpecificationExecutor<Users>{
    // findByEmail
    Optional<Users> findByEmail(String email);

    // findByUsername
    Optional<Users> findByUsername(String username);
}
