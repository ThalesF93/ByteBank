package br.com.bytebank.customers.infrastructure.repositories;

import br.com.bytebank.customers.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {


    boolean existsByCpf(String cpf);
}
