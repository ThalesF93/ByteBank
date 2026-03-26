package br.com.coderbank.operacoes_bancarias.repositories.contas;

import br.com.coderbank.operacoes_bancarias.entities.contas.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
