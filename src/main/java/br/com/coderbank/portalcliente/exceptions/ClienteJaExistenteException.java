package br.com.coderbank.portalcliente.exceptions;

public class ClienteJaExistenteException extends RuntimeException {
    public ClienteJaExistenteException(String message) {
        super(message);
    }
}
