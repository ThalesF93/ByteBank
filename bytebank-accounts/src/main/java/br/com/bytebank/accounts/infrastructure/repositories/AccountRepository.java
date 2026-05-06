package br.com.bytebank.accounts.infrastructure.repositories;

import br.com.bytebank.accounts.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

}
