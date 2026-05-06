package br.com.bytebank.accounts.domain.exception.handler;

import br.com.bytebank.accounts.domain.exception.AccountNotFoundException;
import br.com.bytebank.accounts.domain.exception.CustomerNotFoundException;
import br.com.bytebank.accounts.domain.exception.SameAccountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(final MethodArgumentNotValidException exception){

        Map<String, String> validationErrors = buildValidationErrorResponse(exception);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation error"
        );

        problemDetail.setTitle("Invalid data");
        problemDetail.setProperty("errors", validationErrors);
        problemDetail.setType(URI.create("https://api.coderbank.com.br/errors/validation"));

        return problemDetail;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFound(final Throwable exception){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        problemDetail.setTitle("Account not found");
        problemDetail.setType(URI.create("https://api.coderbank.com.br/errors/not_found"));

        return problemDetail;
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleCustomerNotFound(final Throwable exception){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        problemDetail.setTitle("Customer not found");
        problemDetail.setType(URI.create("https://api.coderbank.com.br/errors/not_found"));

        return problemDetail;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(SameAccountException.class)
    public ProblemDetail handleSameAccount(final Throwable exception){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );

        problemDetail.setTitle("The accounts must be different");
        problemDetail.setType(URI.create("https://api.coderbank.com.br/errors/conflict"));

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
