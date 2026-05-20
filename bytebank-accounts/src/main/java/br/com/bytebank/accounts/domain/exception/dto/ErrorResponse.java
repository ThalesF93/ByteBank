package br.com.bytebank.accounts.domain.exception.dto;

import java.time.Instant;

public record ErrorResponse(
        String code,
        String message,
        int status,
        String path,
        Instant timestamp
) {
    public static ErrorResponse of(String code, String message, int status, String path) {
        return new ErrorResponse(code, message, status, path, Instant.now());
    }
}
