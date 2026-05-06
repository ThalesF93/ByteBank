package br.com.bytebank.accounts.domain.exception;

public class ClosingAccountException extends RuntimeException {
    public ClosingAccountException(String message) {
        super(message);
    }
}
