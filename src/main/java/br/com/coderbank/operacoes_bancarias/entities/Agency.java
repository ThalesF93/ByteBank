package br.com.coderbank.operacoes_bancarias.entities;

import org.thales.exceptions.AccountNotFoundException;
import org.thales.exceptions.ClosingAccountException;
import org.thales.exceptions.DuplicateAccountException;
import org.thales.model.accounts.Account;
import org.thales.model.employees.Manager;
import org.thales.model.holders.CorporateHolder;
import org.thales.model.holders.Holder;
import org.thales.model.holders.IndividualHolder;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Agency {

    private String name;
    private final String agencyNumber;
    private String address;
    private Manager manager;
    private final Map<String, Account> accounts = new HashMap<>();


    protected Agency(String name, String agencyNumber, String address, Manager manager) {
        this.name = name;
        this.agencyNumber = agencyNumber;
        this.address = address;
        this.manager = manager;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgencyNumber() {
        return agencyNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
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


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Agency agency = (Agency) o;
        return Objects.equals(agencyNumber, agency.agencyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(agencyNumber);
    }

    @Override
    public String toString() {
        return "Agency{" +
                "name='" + name + '\'' +
                ", agencyNumber='" + agencyNumber + '\'' +
                ", address='" + address + '\'' +
                ", manager=" + manager +
                ", accounts=" + accounts +
                '}';
    }
}
