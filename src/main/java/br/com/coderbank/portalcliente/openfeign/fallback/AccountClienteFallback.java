package br.com.coderbank.portalcliente.openfeign.fallback;

import br.com.coderbank.portalcliente.entities.PendingAccountOpening;
import br.com.coderbank.portalcliente.openfeign.dtos.requests.AccountRequestDTO;
import br.com.coderbank.portalcliente.openfeign.feignclients.AccountClient;
import br.com.coderbank.portalcliente.repositories.PendingAccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountClienteFallback implements AccountClient {

    private PendingAccountRepository repository;

    @Override
    public ResponseEntity<Void> openAccount(AccountRequestDTO request) {
        PendingAccountOpening pending = new PendingAccountOpening();
        pending.setClientId(request.customerId());
        pending.setAttempts(0);
        pending.setProcessed(false);
        repository.save(pending);
        return ResponseEntity.ok().build();
    }
}
