package com.playtomic.tests.wallet.usecases.dtos;

public record TopupBalanceUseCaseRequest(String walletId, int amount, String currency, String creditCardNumber, String idempotencyId) {}
