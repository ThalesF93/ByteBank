package br.com.bytebank.customers.entities;


import br.com.bytebank.customers.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @CreationTimestamp
    private String createdDate;

    @Column
    private String createByUser;

    @Column
    private String editedByUser;

    @Column
    @UpdateTimestamp
    private String editDate;


}
