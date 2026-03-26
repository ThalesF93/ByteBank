package br.com.coderbank.operacoes_bancarias.exceptions;

public class InvalidIdentificationException extends RuntimeException {
    public InvalidIdentificationException(String message) {
        super(message);
    }
}
