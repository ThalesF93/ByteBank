package br.com.bytebank.accounts.api.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "DTO to perform withdraw")
public record WithdrawRequestDTO(

        @Schema(description = "Account id to debit")
        @NotBlank
        UUID accountId,

        @Schema(description = "Value to withdraw")
        @Positive
        @NotBlank
        BigDecimal amount
) {
}
