package br.com.bytebank.customers.api.dtos.requests;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;


public record CustomerRequestDTO(

        @NotBlank(message = "The name field is required.")
        @Size(min = 3, message = "The minimum quantity is 3 characters.")
        String name,

        @CPF
        @NotBlank(message = "The CPF field is required.")
        String cpf,

        @Email
        @NotBlank(message = "The EMAIL field is required.")
        String email,

        @NotBlank(message = "The Address field is required.")
        String address,

        @Min(message = "Minimum age is 18 years.", value = 18)
        @Max(message = "Maximum age is 120 years.", value = 120)
        Integer age
) {
}
