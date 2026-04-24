package br.com.bytebank.customers.dtos.responses;



import br.com.bytebank.customers.enums.CustomerStatus;

import java.util.UUID;

public record CustomerShortResponseDTO(

        UUID id,

        String nome,

        CustomerStatus CustomerStatus


) {
}
