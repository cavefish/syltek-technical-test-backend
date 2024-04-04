package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.api.dtos.TopupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class WalletControllerTest {

  WalletController subjet;

  @BeforeEach
  void setup() {
    subjet = new WalletController();
  }

  @Test
  void testGetWallet() {
    // When
    var result = subjet.getWallet();
    // Then
    assertThat(result).isNotNull();
  }

  @Test
  void testTopupWallet() {
    // Given
    TopupRequest request = new TopupRequest(123, "EUR");
    // When
    var result = subjet.topupWallet(request);
    // Then
    assertThat(result)
        .isNotNull()
        .extracting(ResponseEntity::getStatusCode)
        .as("Status Code")
        .satisfies(HttpStatusCode::is4xxClientError);
  }
}
