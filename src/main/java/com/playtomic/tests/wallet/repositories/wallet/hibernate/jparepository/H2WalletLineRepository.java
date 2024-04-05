package com.playtomic.tests.wallet.repositories.wallet.hibernate.jparepository;

import com.playtomic.tests.wallet.repositories.wallet.hibernate.domain.H2WalletLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface H2WalletLineRepository extends JpaRepository<H2WalletLine, String> {}
