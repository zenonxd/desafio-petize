package br.com.moreira.desafiopetize.domain.services.mapper;

import br.com.moreira.desafiopetize.domain.entities.Task;
import br.com.moreira.desafiopetize.interfaces.dtos.CreateTaskRequestDto;
import br.com.moreira.desafiopetize.interfaces.dtos.TaskResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(CreateTaskRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setPriority(dto.priority());
        task.setDueDate(dto.dueDate());

        return task;
    }

    public TaskResponseDTO toResponseDTO(Task entity) {
        if (entity == null) {
            return null;
        }

        return new TaskResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getPriority(),
                entity.getDueDate(),
                entity.getUser().getId(),
                entity.getUser().getUsername()
        );
    }
}
