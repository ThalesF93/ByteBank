package br.com.coderbank.operacoes_bancarias.dtos.transacoes.responses;

import br.com.coderbank.operacoes_bancarias.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDTO(
        UUID id,
        OperationType type,
        BigDecimal amount,
        String description,
        LocalDateTime dateTime

) {}
