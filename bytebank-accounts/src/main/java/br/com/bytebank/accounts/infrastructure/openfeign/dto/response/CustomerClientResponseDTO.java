package br.com.bytebank.accounts.infrastructure.openfeign.dto.response;

import java.util.UUID;

public record CustomerClientResponseDTO(
        UUID id,

        String name
) {
}
