package br.com.coderbank.operacoes_bancarias.dtos.request;

import java.util.UUID;

public record AccountRequestDTO(
        UUID customerId
) {
}
