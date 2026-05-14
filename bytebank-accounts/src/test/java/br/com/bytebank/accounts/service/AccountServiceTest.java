package br.com.bytebank.accounts.service;

import br.com.bytebank.accounts.api.dtos.request.AccountRequestDTO;
import br.com.bytebank.accounts.api.dtos.response.AccountResponseDTO;
import br.com.bytebank.accounts.domain.entity.Account;
import br.com.bytebank.accounts.infrastructure.feignclient.CustomerClient;
import br.com.bytebank.accounts.infrastructure.messaging.AccountEventPublisher;
import br.com.bytebank.accounts.infrastructure.repositories.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    AccountServiceImpl accountService;

    @Mock
    AccountRepository accountRepository;

    @Mock
    AccountEventPublisher eventPublisher;

    @Mock
    CustomerClient customerClient;

    @Test
    @DisplayName("Should open account with customer ID from customerClient")
    void mustOpenAccount(){

        AccountRequestDTO accountDTO = new AccountRequestDTO(UUID.randomUUID());

        AccountResponseDTO result = accountService.openAccount(accountDTO);

        Mockito.verify(accountRepository).save(Mockito.any(Account.class));
        Mockito.verify(eventPublisher).publishAccountOpened(Mockito.eq(accountDTO.customerId()), Mockito.eq(result.accountId()));

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.clientId()).isEqualTo(accountDTO.customerId());
    }

}