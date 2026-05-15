package br.com.bytebank.accounts.infrastructure.repositories;

import br.com.bytebank.accounts.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findAccountsByCustomerId(UUID id);

    boolean existsByCustomerId(UUID id);

    Optional<Account> findAccountByIdAndIsActiveTrue(UUID id);

}
