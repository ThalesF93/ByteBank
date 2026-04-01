package br.com.bytebank.customers.dtos.requests;

import java.util.UUID;

public record CheckingPendingAccountsDTO(
        UUID costumerId
) {
}
