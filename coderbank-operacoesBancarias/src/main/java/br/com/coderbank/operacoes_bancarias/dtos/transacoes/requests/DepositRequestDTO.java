package br.com.coderbank.operacoes_bancarias.dtos.transacoes.requests;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositRequestDTO(


        UUID accountId,

        @Positive
        BigDecimal amount
) {
}
