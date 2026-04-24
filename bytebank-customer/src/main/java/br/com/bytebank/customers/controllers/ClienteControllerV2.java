package br.com.bytebank.customers.controllers;

import br.com.bytebank.customers.dtos.requests.CustomerRequestDTO;
import br.com.bytebank.customers.dtos.responses.CustomerResponseDTO;
import br.com.bytebank.customers.dtos.responses.CustomerShortResponseDTO;
import br.com.bytebank.customers.dtos.responses.PagedResponse;
import br.com.bytebank.customers.dtos.responses.PendingAccountStatusResponse;
import br.com.bytebank.customers.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v2/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteControllerV2 {

    private final CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> save(@Valid @RequestBody CustomerRequestDTO customerRequestDTO){
        log.atInfo()
                .setMessage("Request received. endpoint=POST")
                .addKeyValue("Customer" , customerRequestDTO.name())
                .log();

        var costumer = service.saveCostumer(customerRequestDTO);

        log.info("Request completed. clientId={} status={}", costumer.id(), costumer.customerStatus());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(costumer);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<PendingAccountStatusResponse> checkPendingAccount(@PathVariable UUID id){
        log.info("Checking account status. clientId={}", id);
        var pending = service.checkAccountStatus(id);
        return ResponseEntity.ok(pending);
    }

    @GetMapping
    public PagedResponse<CustomerShortResponseDTO> getCostumers(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size){
        log.info("Obtaining costumers in database");
        var pageable = PageRequest.of(page, size);

        var costumerPage = service.obterClientes(pageable);

        return new PagedResponse<>(costumerPage);
    }
}

