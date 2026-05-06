package br.com.bytebank.customers.application.services;

import br.com.bytebank.customers.api.dtos.requests.CustomerRequestDTO;
import br.com.bytebank.customers.api.dtos.responses.CustomerResponseDTO;
import br.com.bytebank.customers.api.dtos.responses.CustomerShortResponseDTO;
import br.com.bytebank.customers.api.dtos.responses.PendingAccountStatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CustomerService {

    CustomerResponseDTO saveCostumer(CustomerRequestDTO customerRequestDTO);

    PendingAccountStatusResponse checkAccountStatus(UUID uuid);

    Page<CustomerShortResponseDTO> obterClientes(Pageable pageable);
}
