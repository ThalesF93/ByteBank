package br.com.coderbank.operacoes_bancarias.controllers;

import br.com.coderbank.operacoes_bancarias.dtos.contas.request.AccountRequestDTO;
import br.com.coderbank.operacoes_bancarias.dtos.contas.response.AccountResponseDTO;
import br.com.coderbank.operacoes_bancarias.services.contas.AccountService;
import br.com.coderbank.operacoes_bancarias.services.transacoes.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;


    @PostMapping
    public ResponseEntity<AccountResponseDTO> openAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO){

        var account =  accountService.openAccount(accountRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(account);

    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> showAccount(@PathVariable UUID id){
           var account =  accountService.findAccountById(id);

           return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<String> getStatements(@PathVariable UUID id){

      accountService.generateBankStatement(id);
        return ResponseEntity.status(HttpStatus.OK).body("Statement generated successfully");
    }
}
