package dev.commerce.entitys;

import dev.commerce.dtos.common.TypeOTP;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerify {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User userId;
    private String otp;
    private TypeOTP type;
    private LocalDateTime expiredAt;
    private boolean isUsed = false;

    public void ensureId() {
        if(this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
