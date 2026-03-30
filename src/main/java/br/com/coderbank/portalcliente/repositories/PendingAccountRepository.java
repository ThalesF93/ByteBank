package br.com.coderbank.portalcliente.repositories;

import br.com.coderbank.portalcliente.entities.PendingAccountOpening;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PendingAccountRepository extends JpaRepository<PendingAccountOpening, UUID> {

    List<PendingAccountOpening> findByProcessedFalse();
}
