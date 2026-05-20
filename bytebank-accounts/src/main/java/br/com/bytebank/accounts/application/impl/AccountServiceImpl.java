package br.com.bytebank.accounts.application.impl;

import br.com.bytebank.accounts.api.dtos.client.response.CustomerClientResponseDTO;
import br.com.bytebank.accounts.api.dtos.request.AccountRequestDTO;
import br.com.bytebank.accounts.api.dtos.request.DepositRequestDTO;
import br.com.bytebank.accounts.api.dtos.request.WithdrawRequestDTO;
import br.com.bytebank.accounts.api.dtos.response.AccountResponseDTO;
import br.com.bytebank.accounts.api.dtos.response.BalanceResponseDTO;
import br.com.bytebank.accounts.application.service.AccountService;
import br.com.bytebank.accounts.domain.entity.Account;
import br.com.bytebank.accounts.domain.exception.customized_excpetions.*;
import br.com.bytebank.accounts.infrastructure.feignclient.CustomerClient;
import br.com.bytebank.accounts.infrastructure.messaging.AccountEventPublisher;
import br.com.bytebank.accounts.infrastructure.repositories.AccountRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerClient customerClient;
    private final AccountEventPublisher eventPublisher;

    @Transactional
    @Override
    public AccountResponseDTO openAccount(AccountRequestDTO accountRequestDTO){
        Account account = new Account();
        account.setCustomerId(accountRequestDTO.customerId());

        if (accountRepository.existsByAccountNumber(account.getAccountNumber())){
            log.warn("Account already exists for customerId={}, skipping", accountRequestDTO.customerId());
            throw new DuplicateAccountException(UUID.fromString(account.getAccountNumber()));
        }
        accountRepository.save(account);

        log.info("Account opened. accountId={}", account.getId());
        eventPublisher.publishAccountOpened(accountRequestDTO.customerId(), account.getId());

        return new AccountResponseDTO(account.getId(), account.getCustomerId(), account.getAgency(), account.getBalance());
    }

    @Override
    @Cacheable(value = "accounts", key = "#uuid")
    public AccountResponseDTO findAccountById(UUID uuid){
        var account = accountRepository.findById(uuid)
                .orElseThrow(()-> new AccountNotFoundException(uuid));

        return new AccountResponseDTO(account.getId(), account.getCustomerId(), account.getAgency(), account.getBalance());

    }

    @Transactional
    @Override
    public void closeAccount(UUID id){
        Account account = accountRepository.findAccountByIdAndIsActiveTrue(id)
                .orElseThrow(()-> new AccountNotFoundException(id));
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0){
            throw new ClosingAccountException(id);
        }
        account.setActive(false);
        accountRepository.save(account);
        //accountRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "accounts-by-balance", key = "'all'")
    public List<AccountResponseDTO> showAccountsByBalance(){
        return accountRepository.findAll()
                .stream()
                .sorted((a1, a2) -> a1.getBalance().compareTo(a2.getBalance()))
                .map(AccountResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCustomer(UUID id){
        return accountRepository.existsByCustomerId(id);
    }


    @Override
    public CustomerClientResponseDTO findCustomerByAccountId(UUID id){
        var account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        CustomerClientResponseDTO customer;
        try {
            customer = customerClient.findCustomerById((account.getCustomerId()));
        } catch (FeignException.NotFound e) {
            throw new CustomerNotFoundException(account.getCustomerId());
        }

        return new CustomerClientResponseDTO(customer.id(), customer.name(), customer.email());
    }


    @Override
    @CacheEvict(value ={"accounts", "accounts-by-customer", "accounts-by-balance", "balance"}, allEntries = true)
    public void debit(WithdrawRequestDTO withdrawRequestDTO) {
        Account account = getAccount(withdrawRequestDTO.accountId());
        balanceValidation(account, withdrawRequestDTO.amount());

        account.setBalance(account.getBalance().subtract(withdrawRequestDTO.amount()));
        accountRepository.save(account);
    }

    @Override
    @CacheEvict(value ={"accounts", "accounts-by-customer", "accounts-by-balance", "balance"}, allEntries = true)
    public void credit(DepositRequestDTO depositRequestDTO) {
        Account account = getAccount(depositRequestDTO.accountId());

        account.setBalance(account.getBalance().add(depositRequestDTO.amount()));
        accountRepository.save(account);
    }

    @Override
    @Cacheable(value = "accounts-by-customer", key = "#id")
    public List<AccountResponseDTO> listAccountByCostumer(UUID id) {
        CustomerClientResponseDTO customer;
       
        try {
           customer = customerClient.findCustomerById(id);
        } catch (FeignException.NotFound e) {
            throw new CustomerNotFoundException(id);
        }
        return accountRepository.findAccountsByCustomerId(customer.id())
                .stream()
                .map(account -> new AccountResponseDTO(account.getId(), account.getCustomerId(), account.getAgency(), account.getBalance())).toList();
    }

    @Override
    @Cacheable(value = "balance", key = "#id")
    public BalanceResponseDTO getBalance(UUID id) {
        var account = accountRepository.findById(id).orElseThrow(
                ()-> new AccountNotFoundException(id)
        );

        return new BalanceResponseDTO(account.getBalance());
    }

    protected void balanceValidation(Account account, BigDecimal amount) {
        if (isBalanceInsufficient(account, amount)){
            log.warn("Withdraw must not be more than balance, value={}, balance={}", amount, account.getBalance());
            throw new InsufficientBalanceException(account.getId());
        }
    }

    private static boolean isBalanceInsufficient(Account account, BigDecimal amount) {
        return account.getBalance().compareTo(amount) < 0;
    }

    private Account getAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }




}
