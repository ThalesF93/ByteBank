package br.com.bytebank.customers.api.dtos.client.requests;

import java.util.UUID;

public record AccountRequestDTO(
        UUID customerId
) {
}
