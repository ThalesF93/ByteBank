package br.com.coderbank.portalcliente.repositories;

import br.com.coderbank.portalcliente.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {


    boolean existsByCpf(String cpf);
}
