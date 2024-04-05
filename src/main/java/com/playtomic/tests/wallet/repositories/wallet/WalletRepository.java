package com.playtomic.tests.wallet.repositories.wallet;

import java.util.Optional;

public interface WalletRepository {

    void addBalanceLine(String walletId, int amount, String concept);

    Optional<String> findWalletIdForUserId(String userId);
}
