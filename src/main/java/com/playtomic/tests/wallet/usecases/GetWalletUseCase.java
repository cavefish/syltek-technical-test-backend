package com.playtomic.tests.wallet.usecases;

import com.playtomic.tests.wallet.domain.WalletLine;
import com.playtomic.tests.wallet.repositories.wallet.WalletRepository;
import com.playtomic.tests.wallet.usecases.dtos.GetWalletUseCaseRequest;
import com.playtomic.tests.wallet.usecases.dtos.GetWalletUseCaseResponse;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetWalletUseCase {

    private final WalletRepository walletRepository;

    public GetWalletUseCaseResponse process(GetWalletUseCaseRequest input) {
        var wallet = walletRepository.getWallet(input.walletId());
        if (wallet.isEmpty()) return null;
        int totalAmount = wallet.get().walletLines().stream().mapToInt(WalletLine::amount).sum();
        return new GetWalletUseCaseResponse(wallet.get().walletId(), totalAmount, "EUR");
    }

}
