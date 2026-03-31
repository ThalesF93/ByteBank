package br.com.coderbank.operacoes_bancarias.controllers;

import br.com.coderbank.operacoes_bancarias.dtos.request.AccountRequestDTO;
import br.com.coderbank.operacoes_bancarias.dtos.request.DepositRequestDTO;
import br.com.coderbank.operacoes_bancarias.dtos.request.WithdrawRequestDTO;
import br.com.coderbank.operacoes_bancarias.dtos.response.AccountResponseDTO;
import br.com.coderbank.operacoes_bancarias.services.AccountService;
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


    private final AccountService accountService;


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
