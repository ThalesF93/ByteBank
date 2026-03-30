package br.com.coderbank.operacoes_bancarias.services.contas;

import br.com.coderbank.operacoes_bancarias.dtos.contas.request.AccountRequestDTO;
import br.com.coderbank.operacoes_bancarias.dtos.contas.response.AccountResponseDTO;
import br.com.coderbank.operacoes_bancarias.dtos.transacoes.responses.TransactionResponseDTO;
import br.com.coderbank.operacoes_bancarias.entities.Account;
import br.com.coderbank.operacoes_bancarias.exceptions.AccountNotFoundException;
import br.com.coderbank.operacoes_bancarias.exceptions.ClosingAccountException;
import br.com.coderbank.operacoes_bancarias.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT)
            .withLocale(Locale.US);

    private static final DecimalFormat US_FORMATTER = new DecimalFormat("¤#,##0.00",
            new DecimalFormatSymbols(Locale.US));

    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponseDTO openAccount(AccountRequestDTO accountRequestDTO){

        Account account = new Account();

        account.setCustomerId(accountRequestDTO.customerId());

        accountRepository.save(account);
        log.info("Account opened. accountId={}", account.getId());
        return new AccountResponseDTO(account.getId(), account.getCustomerId(), account.getAgency(), account.getBalance());
    }

    public AccountResponseDTO findAccountById(UUID uuid){
        var account = accountRepository.findById(uuid)
                .orElseThrow(()-> new AccountNotFoundException("Account not found"));

        return new AccountResponseDTO(account.getId(), account.getCustomerId(), account.getAgency(), account.getBalance());

    }

    @Transactional
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

    @Transactional
    public List<TransactionResponseDTO> generateBankStatement(UUID id)  {
        Account account = accountRepository.findById(id)
                .orElseThrow(()-> new AccountNotFoundException("Account not found"));

        return account.getTransactions()
                .stream()
                .map(t-> new TransactionResponseDTO(t.getId(), t.getType(), t.getAmount(), t.getDescription(), t.getDateTime()))
                .toList();
    }
}
