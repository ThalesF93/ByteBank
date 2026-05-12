package br.com.bytebank.accounts.api.dtos.response;

import java.io.Serializable;
import java.math.BigDecimal;

public record BalanceResponseDTO(
        BigDecimal amount
) implements Serializable {
}
