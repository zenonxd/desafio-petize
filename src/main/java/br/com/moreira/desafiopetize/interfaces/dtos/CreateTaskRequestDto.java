package br.com.moreira.desafiopetize.interfaces.dtos;

import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link br.com.moreira.desafiopetize.domain.entities.Task}
 */
public record CreateTaskRequestDto(
        @NotNull(message = "Title can't be null.") @NotEmpty(message = "Title can't be empty.") @NotBlank(message = "Title can't be blank.") String title,
        @NotNull(message = "Description can't be null.") @Size(message = "Description must have more than 5 characters.", min = 5) @NotEmpty(message = "Description can't be empty.") @NotBlank(message = "Description can't be blank.") String description,
        @NotNull(message = "Status can't be null.") TaskStatus status,
        @NotNull(message = "Priority can't be null.") @Positive(message = "Only positive number allowed.") Integer priority,
        @NotNull(message = "Date can't be null.") @FutureOrPresent Date dueDate) implements Serializable {

    public CreateTaskRequestDto(String title, String description, TaskStatus status, Integer priority, Date dueDate) {
        this.title = title;
        this.description = description;
        this.status = status;
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
    public TaskStatus status() {
        return status;
    }

    @Override
    public Integer priority() {
        return priority;
    }

    @Override
    public Date dueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "title = " + title + ", " +
                "description = " + description + ", " +
                "status = " + status + ", " +
                "priority = " + priority + ", " +
                "dueDate = " + dueDate + ")";
    }
}