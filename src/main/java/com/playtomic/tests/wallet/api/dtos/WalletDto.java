package com.playtomic.tests.wallet.api.dtos;

public record WalletDto(
        String walletId,
        int balance,
        String currency
) {
}
