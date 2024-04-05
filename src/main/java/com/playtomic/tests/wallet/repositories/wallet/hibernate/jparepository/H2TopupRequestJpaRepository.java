package com.playtomic.tests.wallet.repositories.wallet.hibernate.jparepository;

import com.playtomic.tests.wallet.repositories.wallet.hibernate.domain.H2TopupRequest;
import com.playtomic.tests.wallet.repositories.wallet.hibernate.domain.H2TopupRequestId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface H2TopupRequestJpaRepository extends JpaRepository<H2TopupRequest, H2TopupRequestId> {}
