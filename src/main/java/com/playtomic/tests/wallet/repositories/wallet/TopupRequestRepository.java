package com.playtomic.tests.wallet.repositories.wallet;

import com.playtomic.tests.wallet.domain.TopupRequest;

public interface TopupRequestRepository {
    TopupRequest atomicInsertIfMissing(String walletId, String idempotencyId);
    void updateRequest(String walletId, String idempotencyId, TopupRequest.State newState);

}
