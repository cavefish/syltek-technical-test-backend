package com.playtomic.tests.wallet.repositories.wallet.hibernate;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletLine;
import com.playtomic.tests.wallet.repositories.wallet.WalletRepository;
import com.playtomic.tests.wallet.repositories.wallet.hibernate.domain.H2Wallet;
import com.playtomic.tests.wallet.repositories.wallet.hibernate.domain.H2WalletLine;
import com.playtomic.tests.wallet.repositories.wallet.hibernate.jparepository.H2WalletLineRepository;
import java.util.Optional;

import com.playtomic.tests.wallet.repositories.wallet.hibernate.jparepository.H2WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class HibernateWalletRepository implements WalletRepository {

  private final H2WalletLineRepository walletLineJpaRepository;
  private final H2WalletRepository walletJpaRepository;

  @Override
  public void addBalanceLine(String walletId, int amount, String concept) {
    walletLineJpaRepository.save(
        H2WalletLine.builder().walletId(walletId).amount(amount).concept(concept).build());
  }

  @Override
  public Optional<String> findWalletIdForUserId(String userId) {
    return walletJpaRepository.findWalletIdByUserId(userId);
  }

  @Override
  public Optional<Wallet> getWallet(String walletId) {
    return walletJpaRepository.findById(walletId).map(this::map);
  }

  private Wallet map(H2Wallet h2Wallet) {
    Wallet.WalletBuilder builder = Wallet.builder();
    for (H2WalletLine line : h2Wallet.getLines()) {
      builder.walletLine(new WalletLine(line.getAmount(), "EUR", line.getConcept()));
    }
    return builder.build();
  }
}
