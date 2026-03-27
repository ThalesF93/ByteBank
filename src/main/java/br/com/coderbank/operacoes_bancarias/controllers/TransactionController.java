package br.com.coderbank.operacoes_bancarias.controllers;

import br.com.coderbank.operacoes_bancarias.dtos.transacoes.requests.DepositRequestDTO;
import br.com.coderbank.operacoes_bancarias.dtos.transacoes.requests.TransferenceRequestDTO;
import br.com.coderbank.operacoes_bancarias.dtos.transacoes.requests.WithdrawRequestDTO;
import br.com.coderbank.operacoes_bancarias.dtos.transacoes.responses.AmountResponse;
import br.com.coderbank.operacoes_bancarias.services.transacoes.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PatchMapping("/deposit")
    public ResponseEntity<String> deposit(@Valid @RequestBody DepositRequestDTO depositRequestDTO){
         transactionService.deposit(depositRequestDTO.accountId(), depositRequestDTO.amount());
         return ResponseEntity.status(HttpStatus.OK).body("Operation successfully done");
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<String> withdraw(@Valid @RequestBody WithdrawRequestDTO withdrawRequestDTO){
         transactionService.deposit(withdrawRequestDTO.accountId(), withdrawRequestDTO.amount());
         return ResponseEntity.status(HttpStatus.OK).body("Operation successfully done");
    }

    @PostMapping
    public ResponseEntity<String> transference(@Valid @RequestBody TransferenceRequestDTO transferenceRequestDTO){

        transactionService.transference(
                transferenceRequestDTO.originAccountId(),
                transferenceRequestDTO.destinationAccountId(),
                transferenceRequestDTO.amount());

        return ResponseEntity.status(HttpStatus.OK).body("Operation successfully done");
    }


}
