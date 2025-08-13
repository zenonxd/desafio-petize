package br.com.moreira.desafiopetize.interfaces.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link br.com.moreira.desafiopetize.domain.entities.Task}
 */
public record CreateSubTaskDTO(
        @NotBlank(message = "Title can't be empty.")
        String title,
        String description,

        @NotNull(message = "Priority can't be null.")
        @Positive(message = "Priority can't be negative.")
        Integer priority,

        @FutureOrPresent(message = "Duedate can't be a past date.")
        LocalDate dueDate,
        @NotNull Long parentId) implements Serializable {
}