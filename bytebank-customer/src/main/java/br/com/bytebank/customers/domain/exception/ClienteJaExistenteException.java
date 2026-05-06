package br.com.bytebank.customers.domain.exception;

public class ClienteJaExistenteException extends RuntimeException {
    public ClienteJaExistenteException(String message) {
        super(message);
    }
}
