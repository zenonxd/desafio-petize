package br.com.moreira.desafiopetize.interfaces.dtos;

import java.time.LocalDateTime;

public record ApiError(
        int status,
        String message,
        LocalDateTime timestamp

) {
}
