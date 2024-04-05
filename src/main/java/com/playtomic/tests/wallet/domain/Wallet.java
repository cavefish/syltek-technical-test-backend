package com.playtomic.tests.wallet.domain;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public record Wallet(String walletId, @Singular List<WalletLine> walletLines) {}
