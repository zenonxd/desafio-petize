package br.com.moreira.desafiopetize.interfaces.dtos;

import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * DTO for {@link br.com.moreira.desafiopetize.domain.entities.Task}
 */
public record CreateTaskRequestDto(
        @NotNull(message = "Title can't be null.") @NotEmpty(message = "Title can't be empty.") @NotBlank(message = "Title can't be blank.") String title,
        @NotNull(message = "Description can't be null.") @Size(message = "Description must have more than 5 characters.", min = 5) @NotEmpty(message = "Description can't be empty.") @NotBlank(message = "Description can't be blank.") String description,
        @NotNull(message = "Priority can't be null.") @Positive(message = "Only positive number allowed.") Integer priority,
        @NotNull(message = "Date can't be null.") @FutureOrPresent LocalDate dueDate,
        Long parentId) implements Serializable {

    public CreateTaskRequestDto(String title, String description, Integer priority, LocalDate dueDate, Long parentId) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.parentId = parentId;
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
    public Long parentId() {
        return parentId;
    }


    @Override
    public String toString() {
        return "CreateTaskRequestDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", parentId=" + parentId +
                '}';
    }
}