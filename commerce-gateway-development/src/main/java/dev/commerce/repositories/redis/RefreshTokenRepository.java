package dev.commerce.repositories.redis;

import dev.commerce.entitys.RefreshToken;
import dev.commerce.entitys.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("refreshTokenRedisRepository")
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, UUID> {

    void deleteByUsersId(Users userId);

    RefreshToken findByUsersId(Users userId);
}
