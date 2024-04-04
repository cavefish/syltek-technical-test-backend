package com.playtomic.tests.wallet.usecases;

import com.playtomic.tests.wallet.usecases.dtos.TopupBalanceUseCaseRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TopupBalanceUseCaseTest {

    @InjectMocks
    private TopupBalanceUseCase subject;

    @Test
    void testProcess() {
        // Given
        var input = new TopupBalanceUseCaseRequest(123, "EUR");
        // When
        var output = subject.process(input);
        // Then
        assertThat(output).isNotNull();
    }
}
