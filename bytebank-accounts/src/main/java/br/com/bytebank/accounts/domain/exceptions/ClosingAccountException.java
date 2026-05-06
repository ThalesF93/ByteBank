package br.com.bytebank.accounts.domain.exceptions;

public class ClosingAccountException extends RuntimeException {
    public ClosingAccountException(String message) {
        super(message);
    }
}
