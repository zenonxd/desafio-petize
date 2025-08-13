package br.com.moreira.desafiopetize.interfaces.dtos;

import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequestDTO(

        @Schema(description = "Task Status", example = "COMPLETED")
        @NotNull(message = "The task status can't be null.")
        TaskStatus status
) {}
