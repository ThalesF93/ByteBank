package br.com.coderbank.operacoes_bancarias.dtos.contas.response;

import br.com.coderbank.operacoes_bancarias.entities.Account;

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
