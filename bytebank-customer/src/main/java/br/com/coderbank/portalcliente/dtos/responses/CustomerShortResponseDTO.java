package br.com.coderbank.portalcliente.dtos.responses;

import br.com.coderbank.portalcliente.enums.CustomerStatus;

import java.util.UUID;

public record CustomerShortResponseDTO(

        UUID id,

        String nome,

        CustomerStatus CustomerStatus


) {
}
