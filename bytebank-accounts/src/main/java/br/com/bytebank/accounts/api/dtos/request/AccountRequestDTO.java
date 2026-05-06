package br.com.bytebank.accounts.api.dtos.request;

import java.util.UUID;

public record AccountRequestDTO(
        UUID customerId
) {
}
