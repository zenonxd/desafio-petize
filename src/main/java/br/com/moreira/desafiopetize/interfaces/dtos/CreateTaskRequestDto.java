package br.com.moreira.desafiopetize.interfaces.dtos;

import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * DTO for {@link br.com.moreira.desafiopetize.domain.entities.Task}
 */
public record CreateTaskRequestDto(
        @Schema(description = "Task Title", example = "Travel to São Paulo")
        @NotBlank(message = "Title can't be blank.")
        String title,


        @Schema(description = "Task Description", example = "Work trip.")
        @Size(message = "Description must have more than 5 characters.", min = 5)
        @NotBlank(message = "Description can't be blank.")
        String description,

        @Schema(description = "Task Priority", example = "1")
        @NotNull(message = "Priority can't be null.")
        @Positive(message = "Only positive number allowed.")
        Integer priority,

        @Schema(description = "When the task is gonna expire.", example = "2025-10-20")
        @NotNull(message = "Date can't be null.")
        @FutureOrPresent
        LocalDate dueDate) implements Serializable {

    public CreateTaskRequestDto(String title, String description, Integer priority, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String description() {
        return description;
    }


    @Override
    public Integer priority() {
        return priority;
    }

    @Override
    public LocalDate dueDate() {
        return dueDate;
    }



    @Override
    public String toString() {
        return "CreateTaskRequestDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                '}';
    }
}