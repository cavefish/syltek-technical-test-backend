package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.api.dtos.TopupRequest;
import com.playtomic.tests.wallet.api.dtos.TopupResponse;
import com.playtomic.tests.wallet.api.dtos.WalletDto;
import com.playtomic.tests.wallet.usecases.GetWalletUseCase;
import com.playtomic.tests.wallet.usecases.TopupBalanceUseCase;
import com.playtomic.tests.wallet.usecases.dtos.GetWalletUseCaseRequest;
import com.playtomic.tests.wallet.usecases.dtos.TopupBalanceUseCaseRequest;
import com.playtomic.tests.wallet.usecases.dtos.TopupBalanceUseCaseResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@AllArgsConstructor
public class WalletController {
  private static final Logger log = LoggerFactory.getLogger(WalletController.class);

  private final GetWalletUseCase getWalletUseCase;
  private final TopupBalanceUseCase topupBalanceUseCase;

  @GetMapping("/{walletId}")
  public WalletDto getWallet(@PathVariable String walletId) {
    // TODO add auth interceptor to capture user ID from JWT, and check permissions
    log.info("GET wallet");
    var input = new GetWalletUseCaseRequest(walletId);
    var output = getWalletUseCase.process(input);
    return new WalletDto(output.walletId(),output.balance(), output.currency());
  }

  @PostMapping("/{walletId}/topup")
  public ResponseEntity<TopupResponse> topupWallet(@PathVariable String walletId, @RequestBody TopupRequest request) {
    // TODO add auth interceptor to capture user ID from JWT, and check permissions
    log.info("POST wallet/topup: %s".formatted(request));
    var output = topupBalanceUseCase.process(mapRequest(walletId, request));
    if (output.successful()) {
      return ResponseEntity.ok(mapResponse(output));
    }
    return ResponseEntity.badRequest().body(mapResponse(output));
  }

  private TopupBalanceUseCaseRequest mapRequest(String walletId, TopupRequest request) {
    return new TopupBalanceUseCaseRequest(walletId, request.amount(), request.currency(), request.creditCardNumber(), request.idempotencyId());
  }

  private TopupResponse mapResponse(TopupBalanceUseCaseResponse output) {
    return new TopupResponse();
  }
}
