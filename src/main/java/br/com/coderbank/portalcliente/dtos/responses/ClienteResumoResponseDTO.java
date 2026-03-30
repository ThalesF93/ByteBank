package br.com.coderbank.portalcliente.dtos.responses;

import br.com.coderbank.portalcliente.entities.enums.Status;

import java.util.UUID;

public record ClienteResumoResponseDTO(

        UUID id,

        String nome,

        Status status


) {
}
