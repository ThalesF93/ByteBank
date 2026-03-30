package br.com.coderbank.portalcliente.dtos.responses;

import br.com.coderbank.portalcliente.enums.costumerStatus;

import java.util.UUID;

public record ClienteResumoResponseDTO(

        UUID id,

        String nome,

        costumerStatus costumerStatus


) {
}
