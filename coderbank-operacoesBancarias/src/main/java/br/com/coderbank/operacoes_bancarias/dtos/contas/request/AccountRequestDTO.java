package br.com.coderbank.operacoes_bancarias.dtos.contas.request;

import java.util.UUID;

public record AccountRequestDTO(
        UUID customerId
) {
}
