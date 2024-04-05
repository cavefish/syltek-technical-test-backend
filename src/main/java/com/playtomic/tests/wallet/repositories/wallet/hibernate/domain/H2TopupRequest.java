package com.playtomic.tests.wallet.repositories.wallet.hibernate.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "topup_requests", uniqueConstraints = {@UniqueConstraint(columnNames = {"walletID", "idempotencyID"})})
@IdClass(H2TopupRequestId.class)
public class H2TopupRequest {

    @Id
    @Column(name = "walletID", nullable = false, length = 20)
    private String walletId;
    @Id
    @Column(name = "idempotencyID", nullable = false, length = 20)
    private String idempotencyId;

    @Column(name = "walletID", nullable = false, length = 10)
    private String state;

}
