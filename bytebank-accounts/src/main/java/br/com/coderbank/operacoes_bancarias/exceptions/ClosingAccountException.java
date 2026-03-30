package br.com.coderbank.operacoes_bancarias.exceptions;

public class ClosingAccountException extends RuntimeException {
    public ClosingAccountException(String message) {
        super(message);
    }
}
