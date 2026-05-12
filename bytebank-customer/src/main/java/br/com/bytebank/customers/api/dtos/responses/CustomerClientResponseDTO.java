package br.com.bytebank.customers.api.dtos.responses;

import java.io.Serializable;
import java.util.UUID;

public record CustomerClientResponseDTO(
        UUID id,

        String name
) implements Serializable {
}
