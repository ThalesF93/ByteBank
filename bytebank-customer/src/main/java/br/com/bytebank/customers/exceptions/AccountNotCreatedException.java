package br.com.bytebank.customers.exceptions;

public class AccountNotCreatedException extends RuntimeException {
    public AccountNotCreatedException(String message) {
        super(message);
    }
}
