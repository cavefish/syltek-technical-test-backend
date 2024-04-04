package com.playtomic.tests.wallet.usecases;

import com.playtomic.tests.wallet.usecases.dtos.TopupBalanceUseCaseRequest;
import com.playtomic.tests.wallet.usecases.dtos.TopupBalanceUseCaseResponse;
import org.springframework.stereotype.Service;

@Service
public class TopupBalanceUseCase {

    public TopupBalanceUseCaseResponse process(TopupBalanceUseCaseRequest input) {
        return new TopupBalanceUseCaseResponse(false);
    }

}
