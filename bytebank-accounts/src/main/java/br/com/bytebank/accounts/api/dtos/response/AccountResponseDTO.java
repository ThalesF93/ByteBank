package br.com.bytebank.accounts.api.dtos.response;

import br.com.bytebank.accounts.domain.entity.Account;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Response with created account")
public record AccountResponseDTO(

        @Schema(description = "Account ID")
        UUID accountId,

        @Schema(description = "Customer ID")
        UUID clientId,

        @Schema(description = "Agency Number")
        String agencyNumber,

        @Schema(description = "Account Balance")
        BigDecimal balance
) implements Serializable {
    public AccountResponseDTO(Account account) {
        this(
                account.getId(),
                account.getCustomerId(),
                account.getAgency(),
                account.getBalance()
        );
}}
