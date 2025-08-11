package br.com.moreira.desafiopetize.exceptions;

import br.com.moreira.desafiopetize.interfaces.dtos.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidateTaskException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ValidateTaskException ex, WebRequest request) {
        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.NOT_FOUND
        );
    }
}
