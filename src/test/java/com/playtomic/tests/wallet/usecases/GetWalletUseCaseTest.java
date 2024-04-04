package com.playtomic.tests.wallet.usecases;

import static org.assertj.core.api.Assertions.assertThat;

import com.playtomic.tests.wallet.usecases.dtos.GetWalletUseCaseRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetWalletUseCaseTest {

    @InjectMocks
    private GetWalletUseCase subject;

    @Test
    void testProcess() {
        // Given
        var input = new GetWalletUseCaseRequest("foo");
        // When
        var result = subject.process(input);
        // Then
        assertThat(result).isNotNull();
    }

}
