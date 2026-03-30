package br.com.coderbank.portalcliente.dtos.responses;

import br.com.coderbank.portalcliente.entities.enums.Status;

import java.util.UUID;

public record ClienteResponseDTO(
        UUID id,

        Status status,

        String criadoPeloUsuario,

        String criadoDataEHora,

        String editadoPeloUsuario,

        String editadoDataEHora,

        String message
) {
}
