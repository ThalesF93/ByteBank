package br.com.coderbank.operacoes_bancarias.dtos.transacoes.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferenceRequestDTO(

        @NotBlank
        UUID originAccountId,

        @NotBlank
        UUID destinationAccountId,

        @Positive
        BigDecimal amount
) {
}
