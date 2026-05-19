package br.com.bytebank.accounts.api.openapi.controller;

import br.com.bytebank.accounts.api.dtos.client.response.CustomerClientResponseDTO;
import br.com.bytebank.accounts.api.dtos.request.AccountRequestDTO;
import br.com.bytebank.accounts.api.dtos.request.DepositRequestDTO;
import br.com.bytebank.accounts.api.dtos.request.WithdrawRequestDTO;
import br.com.bytebank.accounts.api.dtos.response.AccountResponseDTO;
import br.com.bytebank.accounts.api.dtos.response.BalanceResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;


@Tag(name = "Accounts")
public interface AccountControllerOpenApi {

    ResponseEntity<AccountResponseDTO> openAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO);

    ResponseEntity<AccountResponseDTO> findAccount(@PathVariable UUID id);

    ResponseEntity<Void> closeAccount(@PathVariable UUID id);

    ResponseEntity<Void> debit(@RequestBody WithdrawRequestDTO withdrawRequestDTO);

    ResponseEntity<Void> credit(@RequestBody DepositRequestDTO depositRequestDTO);

    ResponseEntity<BalanceResponseDTO> getBalance(@PathVariable UUID id);

    ResponseEntity<List<AccountResponseDTO>> getListAccountsByCustomer(@PathVariable UUID id);

    ResponseEntity<CustomerClientResponseDTO> findCustomerByAccountId(@PathVariable UUID id);
}
