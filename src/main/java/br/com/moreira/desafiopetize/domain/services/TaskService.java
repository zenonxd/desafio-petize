package br.com.moreira.desafiopetize.domain.services;

import br.com.moreira.desafiopetize.domain.entities.Task;
import br.com.moreira.desafiopetize.domain.entities.User;
import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import br.com.moreira.desafiopetize.domain.repositories.TaskRepository;
import br.com.moreira.desafiopetize.domain.services.mapper.TaskMapper;
import br.com.moreira.desafiopetize.exceptions.ValidateTaskException;
import br.com.moreira.desafiopetize.interfaces.dtos.CreateTaskRequestDto;
import br.com.moreira.desafiopetize.interfaces.dtos.TaskResponseDTO;
import br.com.moreira.desafiopetize.interfaces.dtos.UpdateTaskStatusRequestDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {


    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }


    public TaskResponseDTO createTask(@Valid CreateTaskRequestDto dto, User loggedUser) {
        Task newTask = taskMapper.toEntity(dto);

        newTask.setStatus(TaskStatus.PENDENT);
        newTask.setUser(loggedUser);

        if (dto.parentId() != null) {
            Task parentTask = taskRepository.findById(dto.parentId())
                    .orElseThrow(() -> new ValidateTaskException("Parent task not found with id: " + dto.parentId()));
            newTask.setParentTask(parentTask);
        }

        validateEntity(newTask);

        Task savedTask = taskRepository.save(newTask);

        return taskMapper.toResponseDTO(savedTask);
    }

    public List<TaskResponseDTO> listTask(TaskStatus status, Integer priority, LocalDate dueDate, User loggedUser) {
        Task exampleTask = new Task();
        exampleTask.setStatus(status);
        exampleTask.setPriority(priority);
        exampleTask.setDueDate(dueDate);

        exampleTask.setUser(loggedUser);

        Example<Task> example = Example.of(exampleTask);

        List<Task> tasks = taskRepository.findAll(example);

        return tasks.stream()
                .map(taskMapper::toResponseDTO)
                .collect(Collectors.toList());

    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskStatus newStatus) {
        //arrumar excep
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ValidateTaskException("Task not found"));

        if (newStatus == TaskStatus.COMPLETED) {

            boolean hasPendentTask = taskRepository.existsByParentTaskAndStatusNot(
                    task,
                    TaskStatus.COMPLETED
            );

            if (hasPendentTask) {
                //arrumar excep
                throw new ValidateTaskException("Não é possível concluir a tarefa, pois ela possui subtarefas pendentes.");
            }
        }

        task.setStatus(newStatus);

        Task updatedTask = taskRepository.save(task);

        return taskMapper.toResponseDTO(updatedTask);
    }

    private void validateEntity(Task task) {

        if (taskRepository.existsByTitleAndDueDate(task.getTitle(), task.getDueDate())) {
            throw new ValidateTaskException("A task with the same title and due date already exists.");
        }

    }

    public TaskResponseDTO findTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ValidateTaskException("Task not found"));

        return taskMapper.toResponseDTO(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
