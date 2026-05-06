package br.com.bytebank.accounts.domain.exceptions;

public class DuplicateAccountException extends RuntimeException {
    public DuplicateAccountException(String message) {
        super(message);
    }
}
