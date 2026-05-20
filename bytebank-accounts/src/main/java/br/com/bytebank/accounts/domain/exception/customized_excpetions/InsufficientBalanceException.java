package br.com.bytebank.accounts.domain.exception.customized_excpetions;

import br.com.bytebank.accounts.domain.exception.default_exception.DefaultException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class InsufficientBalanceException extends DefaultException {
    public InsufficientBalanceException(UUID uuid) {
        super("INSUFFICIENT_BALANCE", "Not enough balance in account with id = " + uuid, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
