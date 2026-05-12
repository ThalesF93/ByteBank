package br.com.bytebank.accounts.api.dtos.client.response;

import java.io.Serializable;
import java.util.UUID;

public record CustomerClientResponseDTO(
        UUID id,

        String name
) implements Serializable {
}
