package br.com.coderbank.operacoes_bancarias.repositories;

import br.com.coderbank.operacoes_bancarias.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

}
