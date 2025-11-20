package dev.commerce.repositories.jpa;

import dev.commerce.entitys.OtpVerify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OtpVerifyRepository extends JpaRepository<OtpVerify, UUID>, JpaSpecificationExecutor<OtpVerify> {
}
