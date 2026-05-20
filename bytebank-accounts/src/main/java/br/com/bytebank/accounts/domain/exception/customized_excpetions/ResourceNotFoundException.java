package br.com.bytebank.accounts.domain.exception.customized_excpetions;

import br.com.bytebank.accounts.domain.exception.default_exception.DefaultException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ResourceNotFoundException extends DefaultException {
    public ResourceNotFoundException(UUID uuid) {
        super("RESOURCE_NOT_FOUND", "Resource not found", HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message, HttpStatus.NOT_FOUND);
    }

}
