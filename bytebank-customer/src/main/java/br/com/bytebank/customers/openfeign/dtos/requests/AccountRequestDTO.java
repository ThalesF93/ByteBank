package br.com.bytebank.customers.openfeign.dtos.requests;

import java.util.UUID;

public record AccountRequestDTO(
        UUID customerId
) {
}
