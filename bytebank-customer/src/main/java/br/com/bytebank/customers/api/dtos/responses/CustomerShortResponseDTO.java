package br.com.bytebank.customers.api.dtos.responses;



import br.com.bytebank.customers.domain.enums.CustomerStatus;

import java.io.Serializable;
import java.util.UUID;

public record CustomerShortResponseDTO(

        UUID id,

        String name,

        CustomerStatus CustomerStatus


) implements Serializable {
}
