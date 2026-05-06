package br.com.bytebank.customers.infrastructure.repositories;


import br.com.bytebank.customers.domain.entities.PendingAccountOpening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PendingAccountRepository extends JpaRepository<PendingAccountOpening, UUID> {

    List<PendingAccountOpening> findByProcessedFalse();

    boolean existsByClientId(UUID uuid);
}
