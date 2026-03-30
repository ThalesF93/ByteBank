package br.com.coderbank.portalcliente.controllers;

import br.com.coderbank.portalcliente.exceptions.ClienteJaExistenteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice

public class ControllerExceptionHandler {

    @ExceptionHandler(ClienteJaExistenteException.class)
    public ProblemDetail conflict(final ClienteJaExistenteException exception){
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT, 
            exception.getMessage()
        );
        
        problemDetail.setTitle("Cliente já existente");
        problemDetail.setType(URI.create("https://api.coderbank.com.br/errors/cliente-duplicado"));
        
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(final MethodArgumentNotValidException exception){

        Map<String, String> validationErrors = buildValidationErrorResponse(exception);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            "Erro de validação nos campos"
        );
        
        problemDetail.setTitle("Dados inválidos");
        problemDetail.setProperty("errors", validationErrors);
        problemDetail.setType(URI.create("https://api.coderbank.com.br/errors/validation"));

        return problemDetail;
    }

    private static Map<String, String> buildValidationErrorResponse(MethodArgumentNotValidException exception) {
        Map<String, String> validationErrors = new HashMap<>();

        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    validationErrors.put(fieldName, errorMessage);
                });
        return validationErrors;
    }
}
