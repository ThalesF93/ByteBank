package br.com.bytebank.customers.infrastructure.openfeign.dtos.requests;

import java.util.UUID;

public record AccountRequestDTO(
        UUID customerId
) {
}
