package br.com.coderbank.operacoes_bancarias.services.contas;

import br.com.coderbank.operacoes_bancarias.entities.Transaction;
import br.com.coderbank.operacoes_bancarias.entities.contas.Account;
import br.com.coderbank.operacoes_bancarias.entities.holders.CorporateHolder;
import br.com.coderbank.operacoes_bancarias.entities.holders.Holder;
import br.com.coderbank.operacoes_bancarias.entities.holders.IndividualHolder;
import br.com.coderbank.operacoes_bancarias.enums.OperationType;
import br.com.coderbank.operacoes_bancarias.exceptions.*;
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
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;


    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT)
            .withLocale(Locale.US);

    private static final DecimalFormat US_FORMATTER = new DecimalFormat("¤#,##0.00",
            new DecimalFormatSymbols(Locale.US));



    public void withdraw(UUID id, BigDecimal amount){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account Not found"));
        amountValidation(amount);
        balanceValidation(id,amount);
        account.debit(amount);
        account.addTransactions(new Transaction(OperationType.WITHDRAW, amount));
    }

    public void deposit(UUID id, BigDecimal amount){
        amountValidation(amount);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account Not found"));
        account.credit(amount);
        account.addTransactions(new Transaction(OperationType.DEPOSIT, amount));
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

    public Account openAccount(Account account){

        if (doesAccountExist(account)){
            throw new DuplicateAccountException("Account number already exists");
        }
        accounts.put(account.getAccountNumber(), account);
        return account;
    }

    public Account findAccount(String accountNumber){
        Account account = accounts.get(accountNumber);
        if (account == null){
            throw new AccountNotFoundException("Account not found");
        }
        return account;
    }

    public void closeAccount(String accountNumber){
        Account account = findAccount(accountNumber);
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0){
            throw new ClosingAccountException("Cannot close account with balance bigger than 0");
        }
        accounts.remove(accountNumber);
    }

    private boolean doesAccountExist(Account account) {
        return accounts.containsKey(account.getAccountNumber());
    }

    public String showAccountDetails (String accountNumber){
        Account account = accounts.get(accountNumber);
        if (account == null){
            throw new AccountNotFoundException("Account not found");
        }
        return  String.format(
                "Account number %s is located at agency %s.%n" +
                        "The holder is %s, with updated balance $ %.2f",accountNumber, getAgencyNumber(), account.getHolder().getName(), account.getBalance()

        );
    }

    public List<Account> sortByBalance(){
        return accounts
                .values()
                .stream()
                .sorted((a1, a2) -> a1.getBalance().compareTo(a2.getBalance()))
                .collect(Collectors.toList());
    }

    public List<Account> sortByName(){
        return accounts
                .values()
                .stream()
                .sorted(Comparator.comparing(account -> account.getHolder().getName()))
                .collect(Collectors.toList());
    }

    public void showAccountsByBalance(){
        List<Account> accountsByBalance = sortByBalance();
        for (Account account : accountsByBalance) {
            Holder holder = account.getHolder();
            formatAccountInfo(account, holder);
        }
    }

    public void showAccountsByNameAsc() {
        List<Account> accountsByName = sortByName();
        for (Account account : accountsByName) {
            Holder holder = account.getHolder();
            formatAccountInfo(account, holder);
        }

    }

    private static void formatAccountInfo(Account account, Holder holder) {
        if (holder instanceof IndividualHolder individualHolder) {
            System.out.printf("Account number: %s%n Holder: %s, ID number: %s, with Balance %s%n", account.getAccountNumber(), individualHolder.getName(), individualHolder.getCpf() , account.getBalance());
        } else if (holder instanceof CorporateHolder corporateHolder) {
            System.out.printf("Account number: %s%n Holder: %s, ID number: %s, with Balance %s%n", account.getAccountNumber(), corporateHolder.getName(), corporateHolder.getCnpj() , account.getBalance());
        }
    }



}
