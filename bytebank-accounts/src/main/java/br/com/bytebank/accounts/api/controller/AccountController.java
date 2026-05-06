package br.com.bytebank.accounts.api.controller;

import br.com.bytebank.accounts.api.dtos.request.AccountRequestDTO;
import br.com.bytebank.accounts.api.dtos.request.DepositRequestDTO;
import br.com.bytebank.accounts.api.dtos.request.WithdrawRequestDTO;
import br.com.bytebank.accounts.api.dtos.response.AccountResponseDTO;
import br.com.bytebank.accounts.application.impl.AccountServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {


    private final AccountServiceImpl accountService;


    @PostMapping
    public ResponseEntity<AccountResponseDTO> openAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO){
        log.info("Request received. endpoint=POST /accounts customerID={}", accountRequestDTO.customerId());
        var account =  accountService.openAccount(accountRequestDTO);
        log.info("Request complete! Account Opened!");
        return ResponseEntity.status(HttpStatus.CREATED).body(account);

    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> showAccount(@PathVariable UUID id){
        var account =  accountService.findAccountById(id);

        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> closeAccount(@PathVariable UUID id){
        log.info("Request received. endpoint=Delete /accounts accountID={}", id);
        accountService.closeAccount(id);
        log.info("Request completed! Account closed successfully");
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/debit")
    public ResponseEntity<Void> debit(@RequestBody WithdrawRequestDTO withdrawRequestDTO){
        accountService.debit(withdrawRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/credit")
    public ResponseEntity<Void> credit(@RequestBody DepositRequestDTO depositRequestDTO){
        accountService.credit(depositRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
