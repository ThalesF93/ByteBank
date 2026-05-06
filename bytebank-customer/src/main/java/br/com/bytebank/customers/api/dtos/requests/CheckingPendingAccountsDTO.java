package br.com.bytebank.customers.api.dtos.requests;

import java.util.UUID;

public record CheckingPendingAccountsDTO(
        UUID costumerId
) {
}
