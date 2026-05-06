package br.com.bytebank.customers.infrastructure.openfeign.scheduler;


import br.com.bytebank.customers.domain.entity.Customer;
import br.com.bytebank.customers.domain.entity.PendingAccountOpening;
import br.com.bytebank.customers.domain.enums.AccountStatus;
import br.com.bytebank.customers.domain.exception.AccountNotCreatedException;
import br.com.bytebank.customers.infrastructure.openfeign.dtos.requests.AccountRequestDTO;
import br.com.bytebank.customers.infrastructure.feignclient.AccountClient;
import br.com.bytebank.customers.infrastructure.repositories.CustomerRepository;
import br.com.bytebank.customers.infrastructure.repositories.PendingAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AccountRetryScheduler {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PendingAccountRepository pendingAccountRepository;

    @Autowired
    private AccountClient accountClient;

    @Scheduled(fixedDelay = 60000)

    public  void retryPendingAccounts(){

        List<PendingAccountOpening> list = pendingAccountRepository.findByProcessedFalse();

        for (PendingAccountOpening pendingOpening : list){

            try {
                AccountRequestDTO requestDTO = new AccountRequestDTO(pendingOpening.getClientId());
                accountClient.openAccount(requestDTO);

                customerRepository.findById(requestDTO.customerId()).ifPresent(
                        c->{
                            c.setAccountStatus(AccountStatus.CREATED);
                            customerRepository.save(c);
                            log.info("Account created via retry. customerId={}", c.getId());
                        }
                );

                pendingAccountRepository.delete(pendingOpening);
            } catch (AccountNotCreatedException e) {
                pendingOpening.setAttempts(pendingOpening.getAttempts()+1);
                pendingAccountRepository.save(pendingOpening);
            }
        }
    }

}
