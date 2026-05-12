package br.com.bytebank.customers.api.dtos.responses;

import java.util.UUID;

public record CustomerClientResponseDTO(
        UUID id,

        String name
) {
}
