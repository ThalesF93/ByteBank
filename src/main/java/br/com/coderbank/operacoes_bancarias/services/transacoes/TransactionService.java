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

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT)
            .withLocale(Locale.US);

    private static final DecimalFormat US_FORMATTER = new DecimalFormat("¤#,##0.00",
            new DecimalFormatSymbols(Locale.US));

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

    public void generateBankStatement(UUID id)  {
        Account account = getAccount(id, "Account not found");

        File directory = new File("statements");

        if (!directory.exists()) {
            directory.mkdirs();
        }
        File statements = new File(directory, account.getAccountNumber() + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(statements))){

            writer.write("- Account Statement \n");
            writer.write("-----------------------------------------------------\n");
            writer.write("- Statement's Date: " + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "\n");
            writer.write("- Account number " + account.getAccountNumber() + "\n");
            writer.write("- Holder's ID:  " + account.getCustomerId() + "\n");
            writer.write("- Transactions: " + "\n");
            int transactionNumber = 1;
            for (Transaction transaction : account.getTransactions()){
                writer.write( String.format("Date: %s%n #%d -  Transaction: %s, value: %s%n%n", transaction.getDateTime().format(DATE_TIME_FORMATTER), transactionNumber++, transaction.getType(), US_FORMATTER.format(transaction.getAmount()) ));
            }
            writer.write("- Updated Balance: " + US_FORMATTER.format(account.getBalance())+ "\n");
        }  catch (IOException e) {
            throw new RuntimeException("Failed to generate bank statement for account "
                    + account.getAccountNumber(), e);
        }
        showStatement(account);
    }

    private void showStatement(Account account){

        File file = new File("statements/" + account.getAccountNumber() + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error ", e);
        }
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
