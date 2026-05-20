package br.com.bytebank.accounts.api.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "DTO to perform deposit")
public record DepositRequestDTO(

        @Schema(description = "Account id to be deposited")
        @NotBlank
        UUID accountId,

        @Positive
        @Schema(description = "Value to deposit")
        @NotBlank
        BigDecimal amount
) {
}
