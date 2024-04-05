package com.playtomic.tests.wallet.usecases.dtos;

public record GetWalletUseCaseResponse(String walletId, int balance, String currency) {}
