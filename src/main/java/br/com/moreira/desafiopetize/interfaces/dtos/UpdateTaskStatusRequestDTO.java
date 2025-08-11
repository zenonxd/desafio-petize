package br.com.moreira.desafiopetize.interfaces.dtos;

import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequestDTO(
        @NotNull(message = "The task status can't be null.")
        TaskStatus status
) {}
