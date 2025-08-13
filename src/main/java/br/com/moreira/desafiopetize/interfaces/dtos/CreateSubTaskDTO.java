package br.com.moreira.desafiopetize.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
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

        @Schema(description = "Task Title", example = "Travel to São Paulo")
        @NotBlank(message = "Title can't be empty.")
        String title,

        @Schema(description = "Task Description", example = "Work trip.")
        String description,

        @Schema(description = "Task Priority", example = "1")
        @NotNull(message = "Priority can't be null.")
        @Positive(message = "Priority can't be negative.")
        Integer priority,

        @Schema(description = "When the task is gonna expire.", example = "2025-10-20")
        @FutureOrPresent(message = "Duedate can't be a past date.")
        LocalDate dueDate,

        @Schema(description = "Parent Task ID", example = "1")
        @NotNull Long parentId) implements Serializable {
}