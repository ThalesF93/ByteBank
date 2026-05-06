package br.com.bytebank.customers.api.dtos.responses;



import br.com.bytebank.customers.domain.entity.Customer;
import br.com.bytebank.customers.domain.enums.CustomerStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponseDTO(
        UUID id,

        CustomerStatus customerStatus,

        String createByUser,

        LocalDateTime createdDate,

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
