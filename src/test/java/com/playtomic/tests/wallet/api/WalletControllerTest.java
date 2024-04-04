package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.api.dtos.TopupRequest;
import com.playtomic.tests.wallet.api.dtos.WalletDto;
import com.playtomic.tests.wallet.usecases.GetWalletUseCase;
import com.playtomic.tests.wallet.usecases.TopupBalanceUseCase;
import com.playtomic.tests.wallet.usecases.dtos.GetWalletUseCaseResponse;
import com.playtomic.tests.wallet.usecases.dtos.TopupBalanceUseCaseRequest;
import com.playtomic.tests.wallet.usecases.dtos.TopupBalanceUseCaseResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {

  @Mock private GetWalletUseCase getWalletUseCase;
  @Mock private TopupBalanceUseCase topupBalanceUseCase;
  @InjectMocks private WalletController subject;

  @Test
  void testGetWallet() {
    // Given
    when(getWalletUseCase.process(any())).thenReturn(new GetWalletUseCaseResponse(123, "EUR"));
    WalletDto expectedResult = new WalletDto(123, "EUR");
    // When
    var result = subject.getWallet();
    // Then
    verify(getWalletUseCase).process(any());
    assertThat(result).isNotNull().isEqualTo(expectedResult);
  }

  @Test
  void testTopupWalletFails() {
    // Given
    TopupRequest request = new TopupRequest(123, "EUR");
    when(topupBalanceUseCase.process(eq(new TopupBalanceUseCaseRequest(123, "EUR"))))
        .thenReturn(new TopupBalanceUseCaseResponse(false));
    // When
    var result = subject.topupWallet(request);
    // Then
    verify(topupBalanceUseCase).process(any());
    assertThat(result)
        .isNotNull()
        .extracting(ResponseEntity::getStatusCode)
        .as("Status Code")
        .matches(HttpStatusCode::is4xxClientError);
  }

  @Test
  void testTopupWalletSuccessful() {
    // Given
    TopupRequest request = new TopupRequest(123, "EUR");
    when(topupBalanceUseCase.process(eq(new TopupBalanceUseCaseRequest(123, "EUR"))))
        .thenReturn(new TopupBalanceUseCaseResponse(true));
    // When
    var result = subject.topupWallet(request);
    // Then
    verify(topupBalanceUseCase).process(any());
    assertThat(result)
        .isNotNull()
        .extracting(ResponseEntity::getStatusCode)
        .as("Status Code")
        .matches(HttpStatusCode::is2xxSuccessful);
  }
}
