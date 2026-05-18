package br.com.bytebank.customers.service;

import br.com.bytebank.customers.api.dtos.requests.CustomerRequestDTO;
import br.com.bytebank.customers.api.dtos.requests.CustomerUpdateDTO;
import br.com.bytebank.customers.api.dtos.responses.CustomerClientResponseDTO;
import br.com.bytebank.customers.api.dtos.responses.CustomerShortResponseDTO;
import br.com.bytebank.customers.application.impl.CustomerServiceImpl;
import br.com.bytebank.customers.domain.entity.Customer;
import br.com.bytebank.customers.domain.exception.CustomerNotFoundException;
import br.com.bytebank.customers.domain.exception.DuplicateCustomerException;
import br.com.bytebank.customers.infrastructure.messaging.CustomerEventPublisher;
import br.com.bytebank.customers.infrastructure.repositories.CustomerRepository;
import br.com.bytebank.customers.tests_builders.CustomerTestsBuilders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@InjectMocks
	CustomerServiceImpl customerService;

	@Mock
	CustomerRepository customerRepository;

	@Mock
	CustomerEventPublisher eventPublisher;

	@Test
	@DisplayName("Should create a customer successfully")
	void mustCreateCustomer() {
		CustomerRequestDTO dto = CustomerTestsBuilders.customerRequestDTOBuilder();
		var entity = CustomerTestsBuilders.createEntityWithDTO(dto);

		var result = customerService.createCustomer(dto);

		verify(customerRepository).save(entity);
		verify(eventPublisher).publishCustomerCreated(entity.getId());

		assertThat(dto.name()).isEqualTo(entity.getName());
	}

	@Test
	@DisplayName("Should throw Exception when passing a duplicated cpf")
	void mustThrowException(){
		CustomerRequestDTO dto = CustomerTestsBuilders.customerRequestDTOBuilder();
		when(customerRepository.existsByCpf(dto.cpf())).thenReturn(true);

		assertThatExceptionOfType(DuplicateCustomerException.class)
				.isThrownBy(()-> customerService.createCustomer(dto))
						.withMessage("Customer with cpf " + dto.cpf() + " already exists");

		verify(customerRepository, never()).save(any(Customer.class));

	}
	@Test
	@DisplayName("Should return a page of CustomerShortResponseDTO")
	void mustReturnPageOfCustomers() {
		Customer customer = new Customer();
		customer.setId(UUID.randomUUID());
		customer.setName("João Silva");

		Pageable pageable = PageRequest.of(0, 10);
		Page<Customer> page = new PageImpl<>(List.of(customer), pageable, 1);

		when(customerRepository.findAll(pageable)).thenReturn(page);

		Page<CustomerShortResponseDTO> result = customerService.getCustomers(pageable);

		assertThat(result).isNotNull();
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent().get(0).name()).isEqualTo("João Silva");
	}

	@Test
	@DisplayName("Should return empty page when no customers exist")
	void mustReturnEmptyPage() {
		Pageable pageable = PageRequest.of(0, 10);

		when(customerRepository.findAll(pageable)).thenReturn(Page.empty());

		Page<CustomerShortResponseDTO> result = customerService.getCustomers(pageable);

		assertThat(result).isEmpty();
		verify(customerRepository).findAll(pageable);
	}

	@Test
	@DisplayName("Should uptade")
	void mustUpdateCustomer(){
		CustomerUpdateDTO dto = CustomerTestsBuilders.customerUpdateDTOBuilder();
		UUID id = UUID.randomUUID();
		Customer customer = CustomerTestsBuilders.createEntity();

		when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

		var result = customerService.updateCustomer(id, dto);

		verify(customerRepository, times(1)).save(customer);

		assertThat(result.name()).isEqualTo(customer.getName());
	}

	@Test
	@DisplayName("Should throw Customer Not found Exception")
	void mustThrowExceptionOnFindingCustomer(){
		CustomerUpdateDTO dto = CustomerTestsBuilders.customerUpdateDTOBuilder();
		UUID id = UUID.randomUUID();

		when(customerRepository.findById(id)).thenReturn(Optional.empty());

		assertThatExceptionOfType(CustomerNotFoundException.class)
				.isThrownBy(() -> customerService.updateCustomer(id, dto))
				.withMessage("Customer id not found" + id);

		verify(customerRepository, never()).save(any());

	}

	@Test
	@DisplayName("Should return customer client response by id")
	void shouldReturnCustomerById() {
		UUID id = UUID.randomUUID();

		Customer customer = CustomerTestsBuilders.createEntity();
		customer.setId(id);

		when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

		CustomerClientResponseDTO result = customerService.findCustomerById(id);

		assertThat(result.id()).isEqualTo(id);
		assertThat(result.name()).isEqualTo(customer.getName());
		assertThat(result.email()).isEqualTo(customer.getEmail());

		verify(customerRepository).findById(id);
	}
	@Test
	@DisplayName("Should throw CustomerNotFoundException when customer does not exist")
	void shouldThrowWhenCustomerNotFound() {
		UUID id = UUID.randomUUID();

		when(customerRepository.findById(id)).thenReturn(Optional.empty());

		assertThatExceptionOfType(CustomerNotFoundException.class)
				.isThrownBy(() -> customerService.findCustomerById(id))
				.withMessage("Customer Not found. ID= " + id);

		verify(customerRepository).findById(id);
	}

}
