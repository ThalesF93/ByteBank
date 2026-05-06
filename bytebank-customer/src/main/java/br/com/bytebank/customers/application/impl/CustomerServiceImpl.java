package br.com.bytebank.customers.application.impl;

import br.com.bytebank.customers.api.dtos.requests.CustomerRequestDTO;
import br.com.bytebank.customers.api.dtos.responses.CustomerShortResponseDTO;
import br.com.bytebank.customers.api.dtos.responses.PendingAccountStatusResponse;
import br.com.bytebank.customers.application.service.CustomerService;
import br.com.bytebank.customers.domain.entity.Customer;
import br.com.bytebank.customers.domain.entity.PendingAccountOpening;
import br.com.bytebank.customers.domain.enums.AccountStatus;
import br.com.bytebank.customers.domain.enums.CustomerStatus;
import br.com.bytebank.customers.domain.exception.AccountNotCreatedException;
import br.com.bytebank.customers.domain.exception.ClienteJaExistenteException;
import br.com.bytebank.customers.infrastructure.openfeign.dtos.requests.AccountRequestDTO;
import br.com.bytebank.customers.infrastructure.feignclient.AccountClient;
import br.com.bytebank.customers.infrastructure.repositories.CustomerRepository;
import br.com.bytebank.customers.infrastructure.repositories.PendingAccountRepository;
import br.com.bytebank.customers.api.dtos.responses.CustomerResponseDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AccountClient accountClient;
    private final PendingAccountRepository pendingAccountRepository;


    @Override
    @Transactional
    public CustomerResponseDTO saveCostumer(CustomerRequestDTO customerRequestDTO){
        checkDuplicateCPF(customerRequestDTO);
        // BeanUtils.copyProperties(customerRequestDTO, customerEntity);

        var customerEntity = dtoToEntity(customerRequestDTO);

        repository.save(customerEntity);
        log.info("Registered customer. customerId={}", customerEntity.getId());

        try {

            AccountRequestDTO accountRequestDTO = new AccountRequestDTO(customerEntity.getId());
            accountClient.openAccount(accountRequestDTO);

            log.info("Account created successfully. customerId={}", customerEntity.getId());

            return CustomerResponseDTO.accountCreated(customerEntity);

        } catch (FeignException | AccountNotCreatedException e) {
            log.warn("Failed to create account, saving in pending. costumerId={} erro={}",
                    customerEntity.getId(), e.getMessage());

            PendingAccountOpening pending = new PendingAccountOpening();
            pending.setClientId(customerEntity.getId());
            pending.setAttempts(0);
            pendingAccountRepository.save(pending);

            return CustomerResponseDTO.accountPending(customerEntity);
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

    @Override
    public PendingAccountStatusResponse checkAccountStatus(UUID uuid){
        var pending = pendingAccountRepository.existsByClientId(uuid);

        if (pending){
            return new PendingAccountStatusResponse(
                    uuid, AccountStatus.PENDING, "Opening account still in process, try again later"
            );
        }else return new PendingAccountStatusResponse(
                uuid, AccountStatus.CREATED, "Account created successfully"
        );
    }

    @Override
    public Page<CustomerShortResponseDTO> obterClientes(Pageable pageable){
        return repository.findAll(pageable)
                .map(converteParaClienteResumoResponseDTO());
    }

    private static Function<Customer, CustomerShortResponseDTO> converteParaClienteResumoResponseDTO() {
        return customer -> new CustomerShortResponseDTO(
                customer.getId(), customer.getName(), customer.getCustomerStatus()
        );
    }

    private void checkDuplicateCPF(CustomerRequestDTO dto){
        final var cpf = dto.cpf();

        if (repository.existsByCpf(cpf)){
            throw new ClienteJaExistenteException("Customer com o cpf " + cpf + " já existe");
        }
    }

}
