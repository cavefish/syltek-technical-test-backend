package com.playtomic.tests.wallet.usecases.dtos;

public record TopupBalanceUseCaseResponse(boolean successful, String errorMessage, String id) {

    public static TopupBalanceUseCaseResponse unsuccessful(String msg) {
        return new TopupBalanceUseCaseResponse(false, msg, null);
    }

    public static TopupBalanceUseCaseResponse successful(String paymentId) {
        return new TopupBalanceUseCaseResponse(true, null, paymentId);
    }

}
