package com.playtomic.tests.wallet.repositories.wallet.hibernate;

import com.playtomic.tests.wallet.domain.TopupRequest;
import com.playtomic.tests.wallet.repositories.wallet.TopupRequestRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateTopupRequestRepository implements TopupRequestRepository {
    @Override
    public TopupRequest atomicInsertIfMissing(String walletId, String idempotencyId) {
        return null;
    }

    @Override
    public void updateRequest(String walletId, String idempotencyId, TopupRequest.State newState) {

    }
}
