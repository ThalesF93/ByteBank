package br.com.coderbank.portalcliente.exceptions;

public class AccountNotCreatedException extends RuntimeException {
    public AccountNotCreatedException(String message) {
        super(message);
    }
}
