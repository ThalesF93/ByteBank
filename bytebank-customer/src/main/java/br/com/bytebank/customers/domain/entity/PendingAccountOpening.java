package br.com.bytebank.customers.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PendingAccountOpening {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column
    private UUID clientId;

    @Column
    private int attempts;

    @Column
    private boolean processed;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

}
