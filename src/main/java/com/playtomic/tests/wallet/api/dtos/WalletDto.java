package com.playtomic.tests.wallet.api.dtos;

public record WalletDto(
        int balance,
        String currency
) {
}
