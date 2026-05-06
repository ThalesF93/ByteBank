package br.com.bytebank.customers.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class PendingAccountOpening {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private UUID clientId;

    @Column
    private int attempts;

    @Column
    private boolean processed;

}
