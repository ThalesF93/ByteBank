package br.com.coderbank.portalcliente.entities;


import br.com.coderbank.portalcliente.enums.CustomerStatus;
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
    private String nome;


    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column
    private String email;

    @Column
    private Integer idade;

    @Column
    private String endereco;

    @Column
    @Enumerated(EnumType.STRING)
    private CustomerStatus customerStatus;

    @Column
    @CreationTimestamp
    private String criadoDataEHora;

    @Column
    private String criadoPeloUsuario;

    @Column
    private String editadoPeloUsuario;

    @Column
    @UpdateTimestamp
    private String editadoDataEHora;


}
