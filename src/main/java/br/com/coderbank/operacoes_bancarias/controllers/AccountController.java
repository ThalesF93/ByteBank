package br.com.coderbank.operacoes_bancarias.controllers;

import br.com.coderbank.operacoes_bancarias.dtos.contas.request.AccountRequestDTO;
import br.com.coderbank.operacoes_bancarias.dtos.contas.response.AccountResponseDTO;
import br.com.coderbank.operacoes_bancarias.dtos.transacoes.responses.TransactionResponseDTO;
import br.com.coderbank.operacoes_bancarias.services.contas.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getStatements(@PathVariable UUID id){

      var transactions = accountService.generateBankStatement(id);
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }
}
