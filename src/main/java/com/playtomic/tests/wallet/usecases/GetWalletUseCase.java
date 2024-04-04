package com.playtomic.tests.wallet.usecases;

import com.playtomic.tests.wallet.usecases.dtos.GetWalletUseCaseRequest;
import com.playtomic.tests.wallet.usecases.dtos.GetWalletUseCaseResponse;
import org.springframework.stereotype.Service;

@Service
public class GetWalletUseCase {

    public GetWalletUseCaseResponse process(GetWalletUseCaseRequest input) {
        return new GetWalletUseCaseResponse(123, "EUR");
    }

}
