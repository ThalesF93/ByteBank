package br.com.bytebank.accounts.exceptions;

public class ClosingAccountException extends RuntimeException {
    public ClosingAccountException(String message) {
        super(message);
    }
}
