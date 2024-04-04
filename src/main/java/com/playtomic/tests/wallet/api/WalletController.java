package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.api.dtos.TopupRequest;
import com.playtomic.tests.wallet.api.dtos.TopupResponse;
import com.playtomic.tests.wallet.api.dtos.WalletDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);

    @GetMapping
    public WalletDto getWallet() {
        // TODO add auth interceptor to capture user ID from JWT, and check permissions
        log.info("GET wallet");
        return new WalletDto();
    }

    @PostMapping("/topup")
    public ResponseEntity<TopupResponse> topupWallet(@RequestBody TopupRequest request) {
        // TODO add auth interceptor to capture user ID from JWT, and check permissions
        log.info("POST wallet/topup: %s".formatted(request));
        return ResponseEntity.badRequest().build();
    }
}
