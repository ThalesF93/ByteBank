package br.com.bytebank.accounts.domain.exception.customized_excpetions;

import br.com.bytebank.accounts.domain.exception.default_exception.DefaultException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class AccountNotFoundException extends DefaultException {

    public AccountNotFoundException(UUID id) {
            super("ACCOUNT_NOT_FOUND", "Account with " + id + "not found", HttpStatus.NOT_FOUND);
    }
}
