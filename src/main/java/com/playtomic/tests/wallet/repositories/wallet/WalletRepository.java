package com.playtomic.tests.wallet.repositories.wallet;

import com.playtomic.tests.wallet.domain.Wallet;

import java.util.Optional;

public interface WalletRepository {

    void addBalanceLine(String walletId, int amount, String concept);

    Optional<Wallet> getWallet(String walletId);
}
