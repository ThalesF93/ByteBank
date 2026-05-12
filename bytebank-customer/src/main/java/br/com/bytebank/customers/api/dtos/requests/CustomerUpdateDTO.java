package br.com.bytebank.customers.api.dtos.requests;

public record CustomerUpdateDTO(

        String name,

        String email,

        String address
) {
}
