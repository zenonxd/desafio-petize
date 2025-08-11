package br.com.moreira.desafiopetize.interfaces.dtos;

import br.com.moreira.desafiopetize.domain.enums.TaskStatus;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        Enum<TaskStatus> status,
        Integer priority,
        java.util.Date dueDate
) {
}
