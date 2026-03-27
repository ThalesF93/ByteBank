package br.com.coderbank.operacoes_bancarias.dtos.transacoes.responses;

import java.math.BigDecimal;

public record AmountResponse(
        BigDecimal amount
) {
}
