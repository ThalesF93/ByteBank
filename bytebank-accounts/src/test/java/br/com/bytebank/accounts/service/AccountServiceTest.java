package br.com.bytebank.accounts.service;

import br.com.bytebank.accounts.api.dtos.client.response.CustomerClientResponseDTO;
import br.com.bytebank.accounts.api.dtos.request.AccountRequestDTO;
import br.com.bytebank.accounts.api.dtos.response.AccountResponseDTO;
import br.com.bytebank.accounts.domain.entity.Account;
import br.com.bytebank.accounts.domain.exception.AccountNotFoundException;
import br.com.bytebank.accounts.domain.exception.ClosingAccountException;
import br.com.bytebank.accounts.domain.exception.CustomerNotFoundException;
import br.com.bytebank.accounts.domain.exception.DuplicateAccountException;
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
import org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.rmi.server.UID;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        verify(accountRepository).save(Mockito.any(Account.class));
        verify(eventPublisher).publishAccountOpened(Mockito.eq(accountDTO.customerId()), Mockito.eq(result.accountId()));

        assertThat(result).isNotNull();
        assertThat(result.clientId()).isEqualTo(accountDTO.customerId());
    }

    @Test
    @DisplayName("Should return Duplicate Account Exception")
    void mustThrowExceptionOnOpeningAccount(){
        AccountRequestDTO accountDTO = new AccountRequestDTO(UUID.randomUUID());
        Mockito.when(accountRepository.existsByCustomerId(accountDTO.customerId())).thenReturn(true);

        assertThatExceptionOfType(DuplicateAccountException.class)
                .isThrownBy(()-> accountService.openAccount(accountDTO));

        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find specific account when informing ID as a parameter")
    void mustFindAccountById(){
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setId(id);
        account.setActive(true);

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        var result = accountService.findAccountById(id);

        assertThat(result).isNotNull();
        assertThat(account.getId()).isEqualTo(result.accountId());
        verify(accountRepository).findById(id);
    }

    @Test
    @DisplayName("Should return Account Not found Exception")
    void mustThrowExceptionWhenFindingById(){
        UUID id = UUID.randomUUID();
        when(accountRepository.findById(id)).thenReturn(Optional.empty());
        assertThatExceptionOfType(AccountNotFoundException.class)
                .isThrownBy(()-> accountService.findAccountById(id));

        verify(accountRepository).findById(id);
    }

    @Test
    @DisplayName("Should inactivate account by method closeAccount")
    void mustCloseAccount(){
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setId(id);
        account.setActive(true);

        when(accountRepository.findAccountByIdAndIsActiveTrue(id)).thenReturn(Optional.of(account));
        accountService.closeAccount(id);

        assertThat(account.isActive()).isEqualTo(false);
    }

    @Test
    @DisplayName("Should throw Account not found exception by passing an inactive account")
    void mustThrowExceptionOnFindingAccountByIdAndIsActive(){
        Account account = new Account();
        account.setActive(false);
        account.setId(UUID.randomUUID());

        when(accountRepository.findAccountByIdAndIsActiveTrue(account.getId())).thenReturn(Optional.empty());

        assertThatExceptionOfType(AccountNotFoundException.class)
                .isThrownBy(()-> accountService.closeAccount(account.getId()))
                .withMessage("Account already inactive or do not exist");

        verify(accountRepository, never()).save(any());
    }


    @Test
    @DisplayName("Should throw Closing Account exception by passing an account with balance bigger tem 0")
    void mustThrowExceptionWhenClosingAccountAndBalanceIsPositive(){
        Account account = new Account();
        account.setActive(false);
        account.setId(UUID.randomUUID());
        account.setBalance(new BigDecimal("10"));

        when(accountRepository.findAccountByIdAndIsActiveTrue(account.getId())).thenReturn(Optional.of(account));

        assertThatExceptionOfType(ClosingAccountException.class)
                .isThrownBy(()-> accountService.closeAccount(account.getId()))
                .withMessage("Cannot close account with balance bigger than 0");

        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return a list with Accounts sorted by balance")
    void mustFindAccountSortedByBalance(){

        Account account = new Account();
        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<AccountResponseDTO> result = accountService.showAccountsByBalance();
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return a boolean if account exist by customer id")
    void mustVerifyIfAccountExistsByCustomerId(){
        UUID id = UUID.randomUUID();
        when(accountRepository.existsByCustomerId(id)).thenReturn(true);

        var result = accountService.existsByCustomer(id);

        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("Should find a customer by passing the account id as a parameter")
    void mustFindCustomerPassingAccountId(){
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setId(id);

        CustomerClientResponseDTO customer = new CustomerClientResponseDTO(UUID.randomUUID(), "any", "any@email.com");

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(customerClient.findCustomerById(account.getCustomerId())).thenReturn(customer);

        var result = accountService.findCustomerByAccountId(id);

        assertThat(result.name()).isEqualTo("any");
    }

    @Test
    @DisplayName("Should throw Account not found exception when passing nonexisting id")
    void mustThrowExceptionWhenFindCustomerByAccountId(){
        UUID id = UUID.randomUUID();

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThatExceptionOfType(AccountNotFoundException.class)
                .isThrownBy(()-> accountService.findCustomerByAccountId(id))
                .withMessage("Account not found");

        verify(customerClient, never()).findCustomerById(UUID.randomUUID());
    }

    @Test
    @DisplayName("Should throw Customer not found exception when passing nonexisting customer id")
    void mustThrowExceptionWhenCustomerClientDoesntFind(){
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setId(id);
        account.setCustomerId(null);

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(customerClient.findCustomerById(account.getCustomerId())).thenThrow(new CustomerNotFoundException("Customer not found id= " + account.getCustomerId()));


        assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(()-> accountService.findCustomerByAccountId(id))
                        .withMessage("Customer not found id= " + account.getCustomerId());
    }

}