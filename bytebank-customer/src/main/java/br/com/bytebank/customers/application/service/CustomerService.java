package br.com.bytebank.customers.application.service;

import br.com.bytebank.customers.api.dtos.requests.CustomerRequestDTO;
import br.com.bytebank.customers.api.dtos.requests.CustomerUpdateDTO;
import br.com.bytebank.customers.api.dtos.responses.CustomerClientResponseDTO;
import br.com.bytebank.customers.api.dtos.responses.CustomerResponseDTO;
import br.com.bytebank.customers.api.dtos.responses.CustomerShortResponseDTO;
import br.com.bytebank.customers.api.dtos.responses.PendingAccountStatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CustomerService {

    CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO);

    Page<CustomerShortResponseDTO> obterClientes(Pageable pageable);

    CustomerUpdateDTO updateCustomer(UUID id, CustomerUpdateDTO customerRequestDTO);

    CustomerClientResponseDTO findCustomerById(UUID id);

}
