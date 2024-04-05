package com.playtomic.tests.wallet.repositories.wallet.hibernate;

import com.playtomic.tests.wallet.domain.TopupRequest;
import com.playtomic.tests.wallet.repositories.wallet.TopupRequestRepository;
import com.playtomic.tests.wallet.repositories.wallet.hibernate.domain.H2TopupRequest;
import com.playtomic.tests.wallet.repositories.wallet.hibernate.domain.H2TopupRequestId;
import com.playtomic.tests.wallet.repositories.wallet.hibernate.jparepository.H2TopupRequestJpaRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@AllArgsConstructor
public class HibernateTopupRequestRepository implements TopupRequestRepository {

    private final static Map<String, TopupRequest.State> stateMappings = new HashMap<>();

    static {
        for (TopupRequest.State state : TopupRequest.State.values()) {
            stateMappings.put(state.name(), state);
        }
    }

    private final H2TopupRequestJpaRepository jpaRepository;

    @Override
    public TopupRequest atomicInsertIfMissing(String walletId, String idempotencyId) {
        // TODO add lock to wallet id to avoid race condition for repeated requests
        var current = jpaRepository.findById(H2TopupRequestId.builder().walletId(walletId).idempotencyId(idempotencyId).build());
        if (current.isPresent()) {
            return map(current.get());
        }
        jpaRepository.save(H2TopupRequest.builder().walletId(walletId).idempotencyId(idempotencyId).state(TopupRequest.State.PENDING.name()).build());
        return new TopupRequest(walletId, idempotencyId, TopupRequest.State.NEW);
    }

    @NotNull
    private static TopupRequest map(H2TopupRequest current) {
        return new TopupRequest(current.getWalletId(), current.getIdempotencyId(), stateMappings.get(current.getState()));
    }

    @Override
    public void updateRequest(String walletId, String idempotencyId, TopupRequest.State newState) {
        jpaRepository.save(H2TopupRequest.builder()
                        .walletId(walletId)
                        .idempotencyId(idempotencyId)
                        .state(newState.name())
                .build());
    }
}
