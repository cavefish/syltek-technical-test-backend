package com.playtomic.tests.wallet.repositories.wallet.hibernate.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wallets")
public class H2Wallet {

    @Id
    @Column(name = "walletID", nullable = false, length = 20)
    private String walletId;

    @Column(name = "ownerID", nullable = false, length = 20, unique = true)
    private String ownerId;

    @OneToMany(mappedBy = "walletId")
    private List<H2WalletLine> lines;

}
