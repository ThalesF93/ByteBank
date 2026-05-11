package br.com.bytebank.customers.infrastructure.scheduler;


import br.com.bytebank.customers.domain.entity.PendingAccountOpening;
import br.com.bytebank.customers.domain.enums.AccountStatus;
import br.com.bytebank.customers.infrastructure.openfeign.dtos.requests.AccountRequestDTO;
import br.com.bytebank.customers.infrastructure.feignclient.AccountClient;
import br.com.bytebank.customers.infrastructure.repositories.CustomerRepository;
import br.com.bytebank.customers.infrastructure.repositories.PendingAccountRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountRetryScheduler {

    private final CustomerRepository customerRepository;
    private final PendingAccountRepository pendingAccountRepository;
    private final AccountClient accountClient;

    private static final int MAX_ATTEMPTS = 5;

    @Scheduled(initialDelay = 30000, fixedDelay = 300000)
    @Transactional
    public  void retryPendingAccounts(){
        log.info("Checking pending account openings for retry");

        List<PendingAccountOpening> list = pendingAccountRepository.findByProcessedFalse();

        if (list.isEmpty()){
            log.info("No pending account found");
            return;
        }

        for (PendingAccountOpening pendingOpening : list){
            AccountRequestDTO requestDTO = new AccountRequestDTO(pendingOpening.getClientId());

            try {

                accountClient.openAccount(requestDTO);
                customerRepository.findById(requestDTO.customerId()).ifPresent(
                        c->{
                            c.setAccountStatus(AccountStatus.CREATED);
                            customerRepository.save(c);
                            log.info("Account created via retry. customerId={}", c.getId());
                        }
                );
                pendingOpening.setProcessed(true);
                pendingAccountRepository.save(pendingOpening);

            } catch (FeignException e) {
                pendingOpening.setAttempts(pendingOpening.getAttempts()+1);
                if (pendingOpening.getAttempts() >= MAX_ATTEMPTS){
                    pendingOpening.setProcessed(true);

                    customerRepository.findById(requestDTO.customerId()).ifPresent(
                            c->{
                                c.setAccountStatus(AccountStatus.CANCELLED);
                                customerRepository.save(c);
                                log.info("Account creation exceeded maximum tries and was cancelled via retry. customerId={}", c.getId());
                            });
                }
                pendingAccountRepository.save(pendingOpening);
            }
        }
    }

}
