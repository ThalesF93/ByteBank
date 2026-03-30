package br.com.coderbank.operacoes_bancarias.dtos.transacoes.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawRequestDTO(


        UUID accountId,

        @Positive
        BigDecimal amount
) {
}
