package br.com.coderbank.portalcliente.dtos.responses;

import br.com.coderbank.portalcliente.entities.Customer;
import br.com.coderbank.portalcliente.enums.CustomerStatus;

import java.util.UUID;

public record CustomerResponseDTO(
        UUID id,

        CustomerStatus customerStatus,

        String createByUser,

        String createdDate,

        String editedByUser,

        String editDate,

        String message
) {
    public static CustomerResponseDTO accountCreated(Customer entity) {
        return new CustomerResponseDTO(
                entity.getId(),
                entity.getCustomerStatus(),
                entity.getCriadoPeloUsuario(),
                entity.getCriadoDataEHora(),
                null,
                null,
                "Registered customer and account created successfully!"
        );
    }
    public static CustomerResponseDTO accountPending(Customer entity) {
        return new CustomerResponseDTO(
                entity.getId(),
                entity.getCustomerStatus(),
                entity.getCriadoPeloUsuario(),
                entity.getCriadoDataEHora(),
                null,
                null,
                "Customer registered! Your account is being created and will be available soon."
        );}

}
