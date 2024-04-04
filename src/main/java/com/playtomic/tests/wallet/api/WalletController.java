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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@AllArgsConstructor
public class WalletController {
  private static final Logger log = LoggerFactory.getLogger(WalletController.class);

  private final GetWalletUseCase getWalletUseCase;
  private final TopupBalanceUseCase topupBalanceUseCase;

  @GetMapping
  public WalletDto getWallet() {
    // TODO add auth interceptor to capture user ID from JWT, and check permissions
    log.info("GET wallet");
    var input = new GetWalletUseCaseRequest("foo");
    var output = getWalletUseCase.process(input);
    return new WalletDto(output.balance(), output.currency());
  }

  @PostMapping("/topup")
  public ResponseEntity<TopupResponse> topupWallet(@RequestBody TopupRequest request) {
    // TODO add auth interceptor to capture user ID from JWT, and check permissions
    log.info("POST wallet/topup: %s".formatted(request));
    var output = topupBalanceUseCase.process(mapRequest(request));
    if (output.succesful()) {
      return ResponseEntity.ok(mapResponse(output));
    }
    return ResponseEntity.badRequest().body(mapResponse(output));
  }

  private TopupBalanceUseCaseRequest mapRequest(TopupRequest request) {
    return new TopupBalanceUseCaseRequest(request.amount(), request.currency());
  }

  private TopupResponse mapResponse(TopupBalanceUseCaseResponse output) {
    return new TopupResponse();
  }
}
