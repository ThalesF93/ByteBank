package br.com.bytebank.customers.api.dtos.responses;



import br.com.bytebank.customers.domain.enums.AccountStatus;

import java.util.UUID;

public record PendingAccountStatusResponse(
        UUID id,

        AccountStatus accountStatus,

        String message
        ) {
}
