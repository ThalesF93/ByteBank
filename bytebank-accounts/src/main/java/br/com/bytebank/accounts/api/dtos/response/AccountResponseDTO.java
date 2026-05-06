package br.com.bytebank.accounts.api.dtos.response;

import br.com.bytebank.accounts.domain.entities.Account;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponseDTO(
        UUID accountId,

        UUID clientId,

        String agencyNumber,

        BigDecimal balance
) {
    public AccountResponseDTO(Account account) {
        this(
                account.getId(),
                account.getCustomerId(),
                account.getAgency(),
                account.getBalance()
        );
}}
