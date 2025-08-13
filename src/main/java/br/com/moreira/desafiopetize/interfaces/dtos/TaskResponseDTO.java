package br.com.moreira.desafiopetize.interfaces.dtos;

import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record TaskResponseDTO(

        @Schema(description = "Task ID", example = "1")
        Long id,

        @Schema(description = "Task Title", example = "Travel to São Paulo")
        String title,

        @Schema(description = "Task Description", example = "Work trip.")
        String description,

        @Schema(description = "Task Status", example = "PENDENT")
        Enum<TaskStatus> status,

        @Schema(description = "Task Priority", example = "1")
        Integer priority,

        @Schema(description = "When the task is gonna expire.", example = "2025-10-20")
        LocalDate dueDate,

        @Schema(description = "User ID", example = "1")
        Long userId,

        @Schema(description = "User Name", example = "John Doe")
        String username,

        @Schema(description = "Subtasks")
        List<TaskResponseDTO> subTasks
) {
}
