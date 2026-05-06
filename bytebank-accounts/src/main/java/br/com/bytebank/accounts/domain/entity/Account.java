package br.com.bytebank.accounts.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Table(name = "accounts")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class Account {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Random RANDOM = new Random();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private UUID customerId;

    @Column
    private String agency;

    @Column
    private String accountNumber;

    @Column
    private BigDecimal balance = BigDecimal.ZERO;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    private String createByUser;

    @Column
    private String editByUser;

    @Column
    @UpdateTimestamp
    private LocalDateTime editedAt;

    public Account() {
        this.accountNumber = generateAccountNumber();
        this.balance = new BigDecimal("0");
        this.agency = generateAgencyNumber();
    }

    private String generateAccountNumber(){
        int number = SECURE_RANDOM.nextInt(90_000_000) + 10_000_000;
        return String.valueOf(number);
    }

    private String generateAgencyNumber(){
        return String.format("%06d", RANDOM.nextInt(900_000) + 100_000);
    }



    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", customerId='" + customerId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
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
