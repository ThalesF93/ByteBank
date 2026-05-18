package br.com.bytebank.customers.tests_builders;

import br.com.bytebank.customers.api.dtos.requests.CustomerRequestDTO;
import br.com.bytebank.customers.api.dtos.requests.CustomerUpdateDTO;
import br.com.bytebank.customers.domain.entity.Customer;
import br.com.bytebank.customers.domain.enums.CustomerStatus;

public class CustomerTestsBuilders {

    public static CustomerRequestDTO customerRequestDTOBuilder() {
        return new CustomerRequestDTO(
                "Mock", "123.456.789-10", "email@email.com", "test", 33
        );
    }

    public static Customer createEntityWithDTO(CustomerRequestDTO customerRequestDTO) {
        var customerEntity = new Customer();
        customerEntity.setName(customerRequestDTO.name());
        customerEntity.setCpf(customerRequestDTO.cpf());
        customerEntity.setAge(customerRequestDTO.age());
        customerEntity.setEmail(customerRequestDTO.email());
        customerEntity.setAddress(customerRequestDTO.address());
        customerEntity.setCustomerStatus(CustomerStatus.ACTIVE);
        return customerEntity;
    }

    public static Customer createEntity() {
        var customerEntity = new Customer();
        customerEntity.setName("Thales");
        customerEntity.setCpf("12345678910");
        customerEntity.setAge(33);
        customerEntity.setEmail("email@email.com");
        customerEntity.setAddress("Brasil");
        customerEntity.setCustomerStatus(CustomerStatus.ACTIVE);
        return customerEntity;
    }
    public static CustomerUpdateDTO customerUpdateDTOBuilder(){
        return new CustomerUpdateDTO("Thales Fernandes", "email@email.com", "Paulista");
    }
}