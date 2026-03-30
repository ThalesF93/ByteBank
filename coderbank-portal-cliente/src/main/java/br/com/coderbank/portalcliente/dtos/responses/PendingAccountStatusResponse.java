package br.com.coderbank.portalcliente.dtos.responses;

import br.com.coderbank.portalcliente.enums.AccountStatus;

import java.util.UUID;

public record PendingAccountStatusResponse(
        UUID id,

        AccountStatus accountStatus,

        String message
        ) {
}
