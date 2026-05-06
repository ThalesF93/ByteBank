package br.com.bytebank.customers.domain.entity;


import br.com.bytebank.customers.domain.enums.AccountStatus;
import br.com.bytebank.customers.domain.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column
    private String email;

    @Column
    private Integer age;

    @Column
    private String address;

    @Column
    @Enumerated(EnumType.STRING)
    private CustomerStatus customerStatus;

    @Column
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column
    private String createByUser;

    @Column
    private String editedByUser;

    @Column
    @UpdateTimestamp
    private LocalDateTime editDate;


}
