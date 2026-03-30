package br.com.coderbank.portalcliente.dtos.requests;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;


public record CustomerRequestDTO(

        @NotBlank(message = "O campo nome é obrigatório")
        @Size(min = 3, message = "A quantidade mínima é de 3 caracteres")
        String nome,

        @CPF
        @NotBlank(message = "O campo CPF é obrigatório")
        String cpf,

        @Email
        @NotBlank(message = "O campo email é obrigatório")
        String email,

        @NotBlank(message = "O campo Endereço é obrigatório")
        String endereco,

        @Min(message = "Idade mínima é de 18 anos", value = 18)
        @Max(message = "Idade máxima é de 120 anos", value = 120)
        Integer idade
) {
}
