package br.com.coderbank.operacoes_bancarias.services.contas;

import br.com.coderbank.operacoes_bancarias.dtos.contas.request.AccountRequestDTO;
import br.com.coderbank.operacoes_bancarias.dtos.contas.response.AccountResponseDTO;
import br.com.coderbank.operacoes_bancarias.entities.Account;
import br.com.coderbank.operacoes_bancarias.entities.Transaction;
import br.com.coderbank.operacoes_bancarias.exceptions.AccountNotFoundException;
import br.com.coderbank.operacoes_bancarias.exceptions.ClosingAccountException;
import br.com.coderbank.operacoes_bancarias.exceptions.CustomerNotFoundException;
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
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class AccountService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT)
            .withLocale(Locale.US);

    private static final DecimalFormat US_FORMATTER = new DecimalFormat("¤#,##0.00",
            new DecimalFormatSymbols(Locale.US));

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

    public void generateBankStatement(UUID id)  {
        Account account = accountRepository.findById(id)
                .orElseThrow(()-> new AccountNotFoundException("Account not found"));

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
}
