package com.playtomic.tests.wallet.domain;

public record TopupRequest(String walletId, String idempotencyId, State state) {
    public enum State {
        NEW,
        PENDING,
        ERROR,
        SUCCESS
    }
}
