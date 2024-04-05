package com.playtomic.tests.wallet.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletLine;
import com.playtomic.tests.wallet.repositories.wallet.WalletRepository;
import com.playtomic.tests.wallet.usecases.dtos.GetWalletUseCaseRequest;
import com.playtomic.tests.wallet.usecases.dtos.GetWalletUseCaseResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GetWalletUseCaseTest {

  public static final String WALLET_ID = "wallet-id";
  @Mock private WalletRepository repository;
  @InjectMocks private GetWalletUseCase subject;

  @Test
  void testProcessWhenWalletMissing() {
    // Given
    var input = new GetWalletUseCaseRequest(WALLET_ID);
    when(repository.getWallet(WALLET_ID)).thenReturn(Optional.empty());
    // When
    var result = subject.process(input);
    // Then
    assertThat(result).isNull();
  }

  @Test
  void testProcessHappyPath() {
    // Given
    var input = new GetWalletUseCaseRequest(WALLET_ID);
    Wallet wallet = Wallet.builder()
            .walletId(WALLET_ID)
            .walletLine(new WalletLine(1_23, "EUR", "topup 1"))
            .walletLine(new WalletLine(1_000_000_00, "EUR", "topup 2"))
            .walletLine(new WalletLine(-1_000_000_00, "EUR", "REFUND 2"))
            .build();
    when(repository.getWallet(WALLET_ID)).thenReturn(Optional.of(wallet));
    // When
    var result = subject.process(input);
    // Then
    assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(new GetWalletUseCaseResponse(WALLET_ID, 1_23, "EUR"));
  }
}
