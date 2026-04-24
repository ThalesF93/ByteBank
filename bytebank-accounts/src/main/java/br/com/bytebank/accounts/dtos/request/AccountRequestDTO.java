package br.com.bytebank.accounts.dtos.request;

import java.util.UUID;

public record AccountRequestDTO(
        UUID customerId
) {
}
