package com.playtomic.tests.wallet.repositories.wallet.hibernate.jparepository;

import com.playtomic.tests.wallet.repositories.wallet.hibernate.domain.H2Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface H2WalletRepository extends JpaRepository<H2Wallet, String> {

    @Query(value = "SELECT w.ownerId FROM wallets w WHERE w.ownerId = ?1")
    Optional<String> findWalletIdByUserId(String userId);

}
