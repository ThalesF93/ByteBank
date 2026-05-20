package br.com.bytebank.accounts.api.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

@Schema(description = "Returns the account balance")
public record BalanceResponseDTO(

        @Schema(description = "Balance value")
        BigDecimal amount
) implements Serializable {
}
