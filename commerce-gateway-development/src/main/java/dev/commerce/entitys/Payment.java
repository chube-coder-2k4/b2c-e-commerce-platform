package dev.commerce.entitys;

import dev.commerce.dtos.common.PaymentMethod;
import dev.commerce.dtos.common.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity{
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;
    @Enumerated(EnumType.STRING)
    private PaymentMethod provider;
    private double amount;
    private String transactionId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDateTime paidAt;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
