package br.com.bytebank.customers.domain.exception;

public class AccountNotCreatedException extends RuntimeException {
    public AccountNotCreatedException(String message) {
        super(message);
    }
}
