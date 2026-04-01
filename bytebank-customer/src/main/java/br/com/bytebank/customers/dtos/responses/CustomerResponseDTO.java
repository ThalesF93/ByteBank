package br.com.bytebank.customers.dtos.responses;



import br.com.bytebank.customers.entities.Customer;
import br.com.bytebank.customers.enums.CustomerStatus;

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
                entity.getCreateByUser(),
                entity.getCreatedDate(),
                null,
                null,
                "Registered customer and account created successfully!"
        );
    }
    public static CustomerResponseDTO accountPending(Customer entity) {
        return new CustomerResponseDTO(
                entity.getId(),
                entity.getCustomerStatus(),
                entity.getCreateByUser(),
                entity.getCreatedDate(),
                null,
                null,
                "Customer registered! Your account is being created and will be available soon."
        );}

}
