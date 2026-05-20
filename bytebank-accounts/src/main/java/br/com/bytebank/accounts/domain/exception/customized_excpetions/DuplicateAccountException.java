package br.com.bytebank.accounts.domain.exception.customized_excpetions;

import br.com.bytebank.accounts.domain.exception.default_exception.DefaultException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class DuplicateAccountException extends DefaultException {
    public DuplicateAccountException(UUID uuid) {
        super("DUPLICATE_ACCOUNT","Account with id = " + uuid + " already exists" , HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
