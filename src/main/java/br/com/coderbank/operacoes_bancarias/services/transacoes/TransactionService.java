package br.com.coderbank.operacoes_bancarias.services.transacoes;

import br.com.coderbank.operacoes_bancarias.entities.Account;
import br.com.coderbank.operacoes_bancarias.entities.Transaction;
import br.com.coderbank.operacoes_bancarias.enums.OperationType;
import br.com.coderbank.operacoes_bancarias.exceptions.AccountNotFoundException;
import br.com.coderbank.operacoes_bancarias.exceptions.InsufficientBalanceException;
import br.com.coderbank.operacoes_bancarias.exceptions.InvalidAmountException;
import br.com.coderbank.operacoes_bancarias.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;



    @Transactional
    public void withdraw(UUID id, BigDecimal amount){
        Account account = getAccount(id, "Account Not found");

        amountValidation(amount);
        balanceValidation(account,amount);

        account.debit(amount);
        account.addTransactions(new Transaction(OperationType.WITHDRAW, amount));

        accountRepository.save(account);
    }

    @Transactional
    public void deposit(UUID id, BigDecimal amount){
        Account account = getAccount(id, "Account Not found");

        amountValidation(amount);

        account.credit(amount);
        account.addTransactions(new Transaction(OperationType.DEPOSIT, amount));

        accountRepository.save(account);
    }

    @Transactional
    public void transference(UUID originAccountId, UUID destinationAccountId, BigDecimal amount){
        Account originAccount = getAccount(originAccountId, "Origin Account not Found");
        Account destinationAccount = getAccount(destinationAccountId, "Destination Account not Found");

        amountValidation(amount);
        balanceValidation(originAccount, amount);

        originAccount.debit(amount);
        destinationAccount.credit(amount);

        originAccount.addTransactions(new Transaction(OperationType.TRANSFER, amount));
        destinationAccount.addTransactions(new Transaction(OperationType.TRANSFER, amount, "Received"));

        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

    }

    private Account getAccount(UUID accountId, String messageError) {
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(messageError));
    }

    protected static void amountValidation(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }

    protected void balanceValidation(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0){
            throw new InsufficientBalanceException("Unauthorized operation! Withdraw must not be more than balance");
        }
    }

}
