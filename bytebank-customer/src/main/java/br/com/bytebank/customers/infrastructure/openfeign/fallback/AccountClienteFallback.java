package br.com.bytebank.customers.infrastructure.openfeign.fallback;


import br.com.bytebank.customers.domain.entities.PendingAccountOpening;
import br.com.bytebank.customers.infrastructure.openfeign.dtos.requests.AccountRequestDTO;
import br.com.bytebank.customers.infrastructure.feignclients.AccountClient;
import br.com.bytebank.customers.infrastructure.repositories.PendingAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountClienteFallback implements AccountClient {


    private final PendingAccountRepository repository;

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
