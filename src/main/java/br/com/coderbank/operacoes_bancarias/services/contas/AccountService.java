package br.com.coderbank.operacoes_bancarias.services.contas;

import br.com.coderbank.operacoes_bancarias.entities.Transaction;
import br.com.coderbank.operacoes_bancarias.entities.contas.Account;
import br.com.coderbank.operacoes_bancarias.enums.OperationType;
import br.com.coderbank.operacoes_bancarias.exceptions.AccountNotFoundException;
import br.com.coderbank.operacoes_bancarias.exceptions.InsufficientBalanceException;
import br.com.coderbank.operacoes_bancarias.exceptions.InvalidAmountException;
import br.com.coderbank.operacoes_bancarias.repositories.contas.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;



    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT)
            .withLocale(Locale.US);

    private static final DecimalFormat US_FORMATTER = new DecimalFormat("¤#,##0.00",
            new DecimalFormatSymbols(Locale.US));

    public void addTransactions(Transaction transaction) {
        transactions.add(transaction);
    }

    public void payment(BigDecimal amount){
        amountValidation(amount);
        balanceValidation(amount);
        this.balance = this.balance.subtract(amount);

    }

    public void withdraw(UUID id, BigDecimal amount){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account Not found"));
        amountValidation(amount);
        balanceValidation(id,amount);
        account.getBalance() = account.getBalance().subtract(amount);
        transactions.add(new Transaction(OperationType.WITHDRAW, amount));
    }

    public void deposit(BigDecimal amount){
        amountValidation(amount);
        this.balance = this.balance.add(amount);
        transactions.add(new Transaction(OperationType.DEPOSIT, amount));
    }

    public void transference(Account destinationAccount, BigDecimal amount){
        amountValidation(amount);
        balanceValidation(amount);
        this.balance = this.balance.subtract(amount);
        destinationAccount.deposit(amount);
        transactions.add(new Transaction(OperationType.TRANSFER, amount));

    }

    public void generateBankStatement()  {
        File directory = new File("statements");

        if (!directory.exists()) {
            directory.mkdirs();
        }
        File statements = new File(directory, getAccountNumber() + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(statements))){

            writer.write("- Bank Statement \n");
            writer.write("-----------------------------------------------------\n");
            writer.write("- Statement's Date: " + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "\n");
            writer.write("- Account number " + this.getAccountNumber() + "\n");
            writer.write("- Holder:  " + getHolder().getName() + "\n");
            writer.write("- Transactions: " + "\n");
            int transactionNumber = 1;
            for (Transaction transaction : this.getTransactions()){
                writer.write( String.format("Date: %s%n #%d -  Transaction: %s, value: %s%n%n", transaction.getDateTime().format(DATE_TIME_FORMATTER), transactionNumber++, transaction.getType(), US_FORMATTER.format(transaction.getAmount()) ));
            }
            writer.write("- Updated Balance: " + US_FORMATTER.format(this.getBalance())+ "\n");
            writeExtraInfo(writer);
        }  catch (IOException e) {
            throw new RuntimeException("Failed to generate bank statement for account "
                    + this.getAccountNumber(), e);
        }
        showStatement();
    }

    protected void showStatement(){

        File file = new File("statements/" + getAccountNumber() + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error ", e);
        }
    }

    protected void writeExtraInfo(BufferedWriter writer) throws IOException {}

    protected static void amountValidation(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0){
            throw new InvalidAmountException("Amount must more than zero");
        }
    }

    protected void balanceValidation(UUID id, BigDecimal amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account Not found"));

        if (account.getBalance().compareTo(amount) < 0){
            throw new InsufficientBalanceException("Unauthorized operation! Withdraw must not be more than balance");
        }
    }


}
