package br.com.coderbank.operacoes_bancarias.entities.contas;


import br.com.coderbank.operacoes_bancarias.entities.Transaction;
import br.com.coderbank.operacoes_bancarias.entities.holders.Holder;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "titular")
    private Holder holder;

    @Column(name = "numeroConta")
    private final String accountNumber;

    @Column(name = "saldo")
    protected BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "transações")
    private final List<Transaction> transactions = new ArrayList<>();

    private static final SecureRandom secureRandom = new SecureRandom();

    public Account(Holder holder) {
        this.holder = Objects.requireNonNull(holder, "Holder Cannot be null");
        this.accountNumber = generateAccountNumber();

    }
    public Holder getHolder() {
        return holder;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    private String generateAccountNumber(){
        int number = secureRandom.nextInt(90_000_000) + 10_000_000;
        return String.valueOf(number);
    }

    @Override
    public String toString() {
        return "Account{" +
                "holder='" + holder + '\'' +
                ", accountNumber=" + accountNumber +
                ", amount=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountNumber);
    }


}
