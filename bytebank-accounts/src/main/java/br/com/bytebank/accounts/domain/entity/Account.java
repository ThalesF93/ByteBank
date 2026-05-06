package br.com.bytebank.accounts.domain.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Random RANDOM = new Random();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
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


}
