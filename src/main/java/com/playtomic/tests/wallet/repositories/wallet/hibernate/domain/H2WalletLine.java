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
@Table(name = "wallet_lines")
public class H2WalletLine {

    @Id
    private Long id;

    @Column(name = "walletID", nullable = false, length = 20)
    private String walletId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "concept",nullable = false, length = 20)
    private String concept;

}
