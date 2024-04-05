package com.playtomic.tests.wallet.repositories.wallet.hibernate;

import com.playtomic.tests.wallet.repositories.wallet.WalletRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class HibernateWalletRepository implements WalletRepository {
    @Override
    public void addBalanceLine(String walletId, int amount, String concept) {

    }

    @Override
    public Optional<String> findWalletIdForUserId(String userId) {
        return Optional.empty();
    }
}
