package br.com.coderbank.portalcliente.entities;


import br.com.coderbank.portalcliente.enums.costumerStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.UUID;

@Entity
@Table(name = "CB_CLIENTE")
public class Cliente {

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
    private costumerStatus costumerStatus;

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


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public costumerStatus getStatus() {
        return costumerStatus;
    }

    public void setStatus(costumerStatus costumerStatus) {
        this.costumerStatus = costumerStatus;
    }

    public String getCriadoDataEHora() {
        return criadoDataEHora;
    }

    public void setCriadoDataEHora(String criadoDataEHora) {
        this.criadoDataEHora = criadoDataEHora;
    }

    public String getCriadoPeloUsuario() {
        return criadoPeloUsuario;
    }

    public void setCriadoPeloUsuario(String criadoPeloUsuario) {
        this.criadoPeloUsuario = criadoPeloUsuario;
    }

    public String getEditadoPeloUsuario() {
        return editadoPeloUsuario;
    }

    public void setEditadoPeloUsuario(String editadoPeloUsuario) {
        this.editadoPeloUsuario = editadoPeloUsuario;
    }

    public String getEditadoDataEHora() {
        return editadoDataEHora;
    }

    public void setEditadoDataEHora(String editadoDataEHora) {
        this.editadoDataEHora = editadoDataEHora;
    }
}
