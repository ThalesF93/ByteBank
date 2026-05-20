package br.com.bytebank.accounts.domain.exception.customized_excpetions;

import br.com.bytebank.accounts.domain.exception.default_exception.DefaultException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class CustomerNotFoundException extends DefaultException {
    public CustomerNotFoundException(UUID uuid) {
        super("CUSTOMER_NOT_FOUND", "Customer with id " + uuid + " not found", HttpStatus.NOT_FOUND);
    }
}
