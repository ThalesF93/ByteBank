package br.com.coderbank.portalcliente.openfeign.dtos.requests;

import java.util.UUID;

public record AccountRequestDTO(
        UUID customerId
) {
}
