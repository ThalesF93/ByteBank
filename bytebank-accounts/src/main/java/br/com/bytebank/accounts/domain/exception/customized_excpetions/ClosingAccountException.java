package br.com.bytebank.accounts.domain.exception.customized_excpetions;

import br.com.bytebank.accounts.domain.exception.default_exception.DefaultException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ClosingAccountException extends DefaultException {
    public ClosingAccountException(UUID id) {
        super("IMPOSSIBLE TO CLOSE ACCOUNT", "Account with id= " + id + " has balance  0 ", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
