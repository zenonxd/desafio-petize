package br.com.moreira.desafiopetize.domain.services;

import br.com.moreira.desafiopetize.domain.entities.Task;
import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import br.com.moreira.desafiopetize.domain.repositories.TaskRepository;
import br.com.moreira.desafiopetize.domain.services.mapper.TaskMapper;
import br.com.moreira.desafiopetize.exceptions.ValidateTaskException;
import br.com.moreira.desafiopetize.interfaces.dtos.CreateTaskRequestDto;
import br.com.moreira.desafiopetize.interfaces.dtos.TaskResponseDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class TaskService {


    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }


    public TaskResponseDTO createTask(@Valid CreateTaskRequestDto dto) {
        Task newTask = taskMapper.toEntity(dto);

        newTask.setStatus(TaskStatus.PENDENT);

        validateEntity(newTask);

        Task savedTask = taskRepository.save(newTask);

        return taskMapper.toResponseDTO(savedTask);
    }

    // Validação de entidade durante criação
    private void validateEntity(Task task) {

        if (taskRepository.existsByTitleAndDueDate(task.getTitle(), task.getDueDate())) {
            throw new ValidateTaskException("A task with the same title and due date already exists.");
        }

    }
}
