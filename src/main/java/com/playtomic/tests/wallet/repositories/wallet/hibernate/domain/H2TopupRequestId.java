package com.playtomic.tests.wallet.repositories.wallet.hibernate.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class H2TopupRequestId implements Serializable {
    private String walletId;
    private String idempotencyId;
}
