package dev.commerce.redis;

import dev.commerce.dtos.common.TypeOTP;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "otp_verify", timeToLive = 300)
public class OtpVerify {
    @Id
    private UUID id;
    private Long userId;
    private String otp;
    private TypeOTP type;
    private boolean used = false;
    private String email;

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
