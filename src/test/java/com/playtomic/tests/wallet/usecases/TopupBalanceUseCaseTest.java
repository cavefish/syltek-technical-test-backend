package com.playtomic.tests.wallet.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.playtomic.tests.wallet.domain.TopupRequest;
import com.playtomic.tests.wallet.repositories.wallet.TopupRequestRepository;
import com.playtomic.tests.wallet.repositories.wallet.WalletRepository;
import com.playtomic.tests.wallet.service.Payment;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;
import com.playtomic.tests.wallet.usecases.dtos.TopupBalanceUseCaseRequest;
import com.playtomic.tests.wallet.usecases.dtos.TopupBalanceUseCaseResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TopupBalanceUseCaseTest {

  public static final String WALLET_ID = "wallet-id";
  @Mock private WalletRepository walletRepository;
  @Mock private TopupRequestRepository topupRequestRepository;
  @Mock private StripeService stripeService;

  @InjectMocks private TopupBalanceUseCase subject;

  @Test
  void testProcessWhenWalletMissing() {
    // Given
    var input = createInput(123);
    when(walletRepository.findWalletIdForUserId(input.userId())).thenReturn(Optional.empty());
    // When
    var output = subject.process(input);
    // Then
    isUnsuccessful(output);
    verify(topupRequestRepository, never()).atomicInsertIfMissing(any(), any());
  }

  @ParameterizedTest
  @EnumSource(
      value = TopupRequest.State.class,
      mode = EnumSource.Mode.EXCLUDE,
      names = {"NEW"})
  void testProcessWhenTopupRequestAlreadyCreated(TopupRequest.State state) {
    // Given
    var input = createInput(123);
    when(walletRepository.findWalletIdForUserId(input.userId())).thenReturn(Optional.of(WALLET_ID));
    when(topupRequestRepository.atomicInsertIfMissing(WALLET_ID, input.idempotencyId()))
        .thenReturn(new TopupRequest(WALLET_ID, input.idempotencyId(), state));
    // When
    var output = subject.process(input);
    // Then
    isUnsuccessful(output);
    verify(stripeService, never()).charge(any(), any());
    verify(topupRequestRepository, never()).updateRequest(any(), any(), any());
  }

  @Test
  void testProcessWhenHappyPath() {
    // Given
    var input = createInput(1_23);
    when(walletRepository.findWalletIdForUserId(input.userId())).thenReturn(Optional.of(WALLET_ID));
    when(topupRequestRepository.atomicInsertIfMissing(WALLET_ID, input.idempotencyId()))
        .thenReturn(new TopupRequest(WALLET_ID, input.idempotencyId(), TopupRequest.State.NEW));
    when(stripeService.charge(input.creditCardNumber(), new BigDecimal("1.23")))
        .thenReturn(new Payment("payment-id"));
    // When
    var output = subject.process(input);
    // Then
    isSuccessful(output);
    verify(topupRequestRepository)
        .updateRequest(WALLET_ID, input.idempotencyId(), TopupRequest.State.SUCCESS);
    verify(walletRepository).addBalanceLine(WALLET_ID, 1_23, "Stripe topup");
  }

  @Test
  void testProcessWhenStripFails() {
    // Given
    var input = createInput(1_23);
    when(walletRepository.findWalletIdForUserId(input.userId())).thenReturn(Optional.of(WALLET_ID));
    when(topupRequestRepository.atomicInsertIfMissing(WALLET_ID, input.idempotencyId()))
            .thenReturn(new TopupRequest(WALLET_ID, input.idempotencyId(), TopupRequest.State.NEW));
    when(stripeService.charge(input.creditCardNumber(), new BigDecimal("1.23")))
        .thenThrow(new StripeServiceException());
    // When
    var output = subject.process(input);
    // Then
    isUnsuccessful(output);
    verify(topupRequestRepository).updateRequest(WALLET_ID, input.idempotencyId(), TopupRequest.State.ERROR);
    verify(walletRepository, never()).addBalanceLine(any(), anyInt(), any());
  }

  private static void isUnsuccessful(TopupBalanceUseCaseResponse output) {
    assertThat(output)
        .usingRecursiveComparison()
        .ignoringExpectedNullFields()
        .isEqualTo(TopupBalanceUseCaseResponse.unsuccessful(null));
  }

  private static void isSuccessful(TopupBalanceUseCaseResponse output) {
    assertThat(output)
        .usingRecursiveComparison()
        .ignoringExpectedNullFields()
        .isEqualTo(TopupBalanceUseCaseResponse.successful(null));
  }

  @NotNull
  private static TopupBalanceUseCaseRequest createInput(int amount) {
    return new TopupBalanceUseCaseRequest("foo", amount, "EUR", "visa-cc", "idempotencyId");
  }
}
