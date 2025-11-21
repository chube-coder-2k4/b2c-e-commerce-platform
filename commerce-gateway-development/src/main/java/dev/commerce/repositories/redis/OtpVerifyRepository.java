package dev.commerce.repositories.redis;

import dev.commerce.redis.OtpVerify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository("otpVerifyRedisRepository")
public interface OtpVerifyRepository extends JpaRepository<OtpVerify, UUID> {
    OtpVerify findByEmail(String email);
}
