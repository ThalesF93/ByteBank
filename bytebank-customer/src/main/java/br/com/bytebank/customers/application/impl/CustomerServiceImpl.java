package br.com.bytebank.customers.application.impl;

import br.com.bytebank.customers.api.dtos.requests.CustomerRequestDTO;
import br.com.bytebank.customers.api.dtos.requests.CustomerUpdateDTO;
import br.com.bytebank.customers.api.dtos.responses.CustomerClientResponseDTO;
import br.com.bytebank.customers.api.dtos.responses.CustomerResponseDTO;
import br.com.bytebank.customers.api.dtos.responses.CustomerShortResponseDTO;
import br.com.bytebank.customers.application.service.CustomerService;
import br.com.bytebank.customers.domain.entity.Customer;
import br.com.bytebank.customers.domain.enums.AccountStatus;
import br.com.bytebank.customers.domain.enums.CustomerStatus;
import br.com.bytebank.customers.domain.exception.ClienteJaExistenteException;
import br.com.bytebank.customers.domain.exception.CustomerNotFoundException;
import br.com.bytebank.customers.infrastructure.messaging.CustomerEventPublisher;
import br.com.bytebank.customers.infrastructure.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerEventPublisher eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = {"account-status", "customers-by-id"}, allEntries = true)
    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO){
        checkDuplicateCPF(customerRequestDTO);

        var customerEntity = dtoToEntity(customerRequestDTO);
        customerEntity.setAccountStatus(AccountStatus.PENDING);
        repository.save(customerEntity);

        eventPublisher.publishCustomerCreated(customerEntity.getId());

        log.info("Registered customer. customerId={}", customerEntity.getId());

        return CustomerResponseDTO.accountPending(customerEntity);
    }


    @Override
    public Page<CustomerShortResponseDTO> obterClientes(Pageable pageable){
        return repository.findAll(pageable)
                .map(converteParaClienteResumoResponseDTO());
    }

    @Override
    @CacheEvict(value = "customers-by-id", allEntries = true)
    public CustomerUpdateDTO updateCustomer(UUID uuid, CustomerUpdateDTO customerUpdateDTO) {
        var customer = repository.findById(uuid).orElseThrow(
                ()-> new CustomerNotFoundException("Customer id not found" + uuid)
        );

        customer.setName(customerUpdateDTO.name());
        customer.setAddress(customerUpdateDTO.address());
        customer.setEmail(customerUpdateDTO.email());
        repository.save(customer);

        return new CustomerUpdateDTO(customerUpdateDTO.name(), customerUpdateDTO.email(), customerUpdateDTO.address());

    }

    @Override
    @Cacheable(value = "customers-by-id", key = "#id")
    public CustomerClientResponseDTO findCustomerById(UUID id) {
        var customer = repository.findById(id).orElseThrow(
                ()-> new CustomerNotFoundException("Customer Not found. ID= " + id)
        );
        return new CustomerClientResponseDTO(customer.getId(), customer.getName(), customer.getEmail());
    }

    private static Function<Customer, CustomerShortResponseDTO> converteParaClienteResumoResponseDTO() {
        return customer -> new CustomerShortResponseDTO(
                customer.getId(), customer.getName(), customer.getCustomerStatus()
        );
    }

    private void checkDuplicateCPF(CustomerRequestDTO dto){
        final var cpf = dto.cpf();

        if (repository.existsByCpf(cpf)){
            throw new ClienteJaExistenteException("Customer with cpf " + cpf + " already exists");
        }
    }

    private static Customer dtoToEntity(CustomerRequestDTO customerRequestDTO) {
        var customerEntity = new Customer();
        customerEntity.setName(customerRequestDTO.name());
        customerEntity.setCpf(customerRequestDTO.cpf());
        customerEntity.setAge(customerRequestDTO.age());
        customerEntity.setEmail(customerRequestDTO.email());
        customerEntity.setAddress(customerRequestDTO.address());



        customerEntity.setCustomerStatus(CustomerStatus.ACTIVE);
        return customerEntity;
    }

}
