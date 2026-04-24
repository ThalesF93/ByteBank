package br.com.bytebank.customers.services;

import br.com.bytebank.customers.dtos.requests.CustomerRequestDTO;
import br.com.bytebank.customers.dtos.responses.CustomerShortResponseDTO;
import br.com.bytebank.customers.dtos.responses.PendingAccountStatusResponse;
import br.com.bytebank.customers.entities.Customer;
import br.com.bytebank.customers.entities.PendingAccountOpening;
import br.com.bytebank.customers.enums.AccountStatus;
import br.com.bytebank.customers.enums.CustomerStatus;
import br.com.bytebank.customers.exceptions.AccountNotCreatedException;
import br.com.bytebank.customers.exceptions.ClienteJaExistenteException;
import br.com.bytebank.customers.openfeign.dtos.requests.AccountRequestDTO;
import br.com.bytebank.customers.openfeign.feignclients.AccountClient;
import br.com.bytebank.customers.repositories.CustomerRepository;
import br.com.bytebank.customers.repositories.PendingAccountRepository;
import br.com.bytebank.customers.dtos.responses.CustomerResponseDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository repository;
    private final AccountClient accountClient;
    private final PendingAccountRepository pendingAccountRepository;


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
