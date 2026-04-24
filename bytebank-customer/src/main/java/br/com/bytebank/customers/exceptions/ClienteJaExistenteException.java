package br.com.bytebank.customers.exceptions;

public class ClienteJaExistenteException extends RuntimeException {
    public ClienteJaExistenteException(String message) {
        super(message);
    }
}
