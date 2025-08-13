package br.com.moreira.desafiopetize.interfaces.dtos;

import br.com.moreira.desafiopetize.domain.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        Enum<TaskStatus> status,
        Integer priority,
        LocalDate dueDate,
        Long userId,
        String username,
        List<TaskResponseDTO> subTasks
) {
}
