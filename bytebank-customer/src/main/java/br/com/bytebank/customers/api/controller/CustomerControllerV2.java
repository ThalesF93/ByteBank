package br.com.bytebank.customers.api.controller;

import br.com.bytebank.customers.api.dtos.requests.CustomerRequestDTO;
import br.com.bytebank.customers.api.dtos.requests.CustomerUpdateDTO;
import br.com.bytebank.customers.api.dtos.responses.*;
import br.com.bytebank.customers.application.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v2/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerControllerV2 {

    private final CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> save(@Valid @RequestBody CustomerRequestDTO customerRequestDTO){
        log.atInfo()
                .setMessage("Request received. endpoint=POST")
                .addKeyValue("Customer" , customerRequestDTO.name())
                .log();

        var costumer = service.createCustomer(customerRequestDTO);

        log.info("Request completed. clientId={} status={}", costumer.id(), costumer.customerStatus());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(costumer);
    }

    @GetMapping
    public PagedResponse<CustomerShortResponseDTO> getCostumers(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size){
        log.info("Obtaining costumers in database");
        var pageable = PageRequest.of(page, size);

        var costumerPage = service.getCustomers(pageable);

        return new PagedResponse<>(costumerPage);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerUpdateDTO> updateCustomer(@PathVariable UUID id, @Valid @RequestBody CustomerUpdateDTO dto){
        log.info("Updating customer from id={}", id);
        return ResponseEntity.ok(service.updateCustomer(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerClientResponseDTO> findCustomerById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findCustomerById(id));
    }
}

