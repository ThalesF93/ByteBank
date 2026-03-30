package br.com.coderbank.portalcliente.dtos.responses;

import br.com.coderbank.portalcliente.enums.costumerStatus;

import java.util.UUID;

public record ClienteResponseDTO(
        UUID id,

        costumerStatus costumerStatus,

        String criadoPeloUsuario,

        String criadoDataEHora,

        String editadoPeloUsuario,

        String editadoDataEHora,

        String message
) {
}
