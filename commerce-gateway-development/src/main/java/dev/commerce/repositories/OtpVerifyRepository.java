package dev.commerce.repositories;

import dev.commerce.entitys.OtpVerify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OtpVerifyRepository extends JpaRepository<OtpVerify, UUID> {
}
