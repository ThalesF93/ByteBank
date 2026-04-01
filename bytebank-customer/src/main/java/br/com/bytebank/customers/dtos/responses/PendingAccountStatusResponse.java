package br.com.bytebank.customers.dtos.responses;



import br.com.bytebank.customers.enums.AccountStatus;

import java.util.UUID;

public record PendingAccountStatusResponse(
        UUID id,

        AccountStatus accountStatus,

        String message
        ) {
}
