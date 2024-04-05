package com.playtomic.tests.wallet.usecases;

import com.playtomic.tests.wallet.domain.TopupRequest;
import com.playtomic.tests.wallet.repositories.wallet.TopupRequestRepository;
import com.playtomic.tests.wallet.repositories.wallet.WalletRepository;
import com.playtomic.tests.wallet.service.Payment;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;
import com.playtomic.tests.wallet.usecases.dtos.TopupBalanceUseCaseRequest;
import com.playtomic.tests.wallet.usecases.dtos.TopupBalanceUseCaseResponse;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TopupBalanceUseCase {

    private static final Logger log = LoggerFactory.getLogger(TopupBalanceUseCase.class);
    public static final BigDecimal BIG_DECIMAL_100 = BigDecimal.valueOf(100);

    private StripeService stripeService;
    private WalletRepository walletRepository;
    private TopupRequestRepository topupRequestRepository;

    public TopupBalanceUseCaseResponse process(TopupBalanceUseCaseRequest input) {
        Optional<String> walletId = walletRepository.findWalletIdForUserId(input.userId());
        if (walletId.isEmpty()) return TopupBalanceUseCaseResponse.unsuccessful("User doesn't have a wallet");

        var topupRequest = topupRequestRepository.atomicInsertIfMissing(walletId.get(), input.idempotencyId());
        if (topupRequest.state() != TopupRequest.State.NEW) {
            String reason = switch (topupRequest.state()) {
                case PENDING -> "Request pending to be processed";
                case ERROR -> "Request failed";
                case SUCCESS -> "Successful request already processed";
                default -> throw new IllegalStateException("Unexpected value: " + topupRequest.state());
            };
            return TopupBalanceUseCaseResponse.unsuccessful(reason);
        }
        log.info("Processing request %s".formatted(input.idempotencyId()));

        TopupBalanceUseCaseResponse result = processRequestToStripe(input, walletId.get());

        TopupRequest.State newState = result.successful()? TopupRequest.State.SUCCESS : TopupRequest.State.ERROR;
        topupRequestRepository.updateRequest(topupRequest.walletId(), topupRequest.idempotencyId(), newState);
        return result;
    }

    @NotNull
    private TopupBalanceUseCaseResponse processRequestToStripe(TopupBalanceUseCaseRequest input, String walletId) {
        BigDecimal amountOnUnits = BigDecimal.valueOf(input.amount()).divide(BIG_DECIMAL_100, 2, RoundingMode.FLOOR);
        Payment payment;
        try {
            payment = stripeService.charge(input.creditCardNumber(), amountOnUnits);
        } catch (StripeServiceException ex) {
            log.error("Failed request %s".formatted(input.idempotencyId()), ex);
            // TODO map known exceptions to proper error messages
            return TopupBalanceUseCaseResponse.unsuccessful("Error processing payment");
        }
        walletRepository.addBalanceLine(walletId, input.amount(), "Stripe topup");
        log.info("Successful request %s".formatted(input.idempotencyId()));
        return TopupBalanceUseCaseResponse.successful(payment.getId());
    }

}
