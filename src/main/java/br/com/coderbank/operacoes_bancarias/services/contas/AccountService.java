package br.com.coderbank.operacoes_bancarias.services.contas;

import br.com.coderbank.operacoes_bancarias.dtos.contas.request.AccountRequestDTO;
import br.com.coderbank.operacoes_bancarias.dtos.contas.response.AccountResponseDTO;
import br.com.coderbank.operacoes_bancarias.entities.Account;
import br.com.coderbank.operacoes_bancarias.exceptions.AccountNotFoundException;
import br.com.coderbank.operacoes_bancarias.exceptions.ClosingAccountException;
import br.com.coderbank.operacoes_bancarias.exceptions.CustomerNotFoundException;
import br.com.coderbank.operacoes_bancarias.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public AccountResponseDTO openAccount(AccountRequestDTO accountRequestDTO){
        // Coloquei uma validação de null, pois ainda não existe a verificação de ID junto ao repositório de clientes.

        if (accountRequestDTO.customerId() == null){
            throw new CustomerNotFoundException("Customer id not found");
        }
        Account account = new Account();

        account.setCustomerId(accountRequestDTO.customerId());

        accountRepository.save(account);

        return new AccountResponseDTO(account.getId(), account.getCustomerId(), account.getAgency(), account.getBalance());
    }

    public AccountResponseDTO findAccountById(UUID uuid){
        var account = accountRepository.findById(uuid)
                .orElseThrow(()-> new AccountNotFoundException("Account not found"));

        return new AccountResponseDTO(account.getId(), account.getCustomerId(), account.getAgency(), account.getBalance());

    }

    public void closeAccount(UUID id){
        Account account = accountRepository.findById(id)
                .orElseThrow(()-> new AccountNotFoundException("Account not found"));
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0){
            throw new ClosingAccountException("Cannot close account with balance bigger than 0");
        }
        accountRepository.deleteById(id);
    }

    public List<AccountResponseDTO> showAccountsByBalance(){
        return accountRepository.findAll()
                .stream()
                .sorted((a1, a2) -> a1.getBalance().compareTo(a2.getBalance()))
                .map(AccountResponseDTO::new)
                .collect(Collectors.toList());
    }

}
