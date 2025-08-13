package br.com.moreira.desafiopetize.domain.services;


import br.com.moreira.desafiopetize.domain.entities.Task;
import br.com.moreira.desafiopetize.domain.entities.User;
import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import br.com.moreira.desafiopetize.domain.repositories.TaskRepository;
import br.com.moreira.desafiopetize.domain.repositories.UserRepository;
import br.com.moreira.desafiopetize.domain.services.mapper.TaskMapper;
import br.com.moreira.desafiopetize.exceptions.PendentSubtaskException;
import br.com.moreira.desafiopetize.interfaces.dtos.CreateTaskRequestDto;
import br.com.moreira.desafiopetize.interfaces.dtos.TaskResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private CreateTaskRequestDto taskRequestDto;
    private User user;
    private Task task;
    private TaskResponseDTO taskResponseDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        taskRequestDto = new CreateTaskRequestDto("Teste", "Descrição", 1, null);
        task = new Task();
        taskResponseDTO = new TaskResponseDTO(
                1L, "Teste",
                "Descrição",
                null, 1,
                null, 1L,
                "testuser", null, null);
    }

    @Test
    @DisplayName("Should create a Parent Task with success")
    void createParentTask_ShouldReturnTaskResponseDTO_WhenSuccessful() {

        when(taskMapper.toEntity(taskRequestDto)).thenReturn(task);
        when(taskRepository.save( any(Task.class))).thenReturn(task);
        when(taskMapper.toResponseDTO(task)).thenReturn(taskResponseDTO);

        TaskResponseDTO result = taskService.createTask(taskRequestDto, String.valueOf(user));

        assertNotNull(result);
        assertEquals(taskResponseDTO.id(), result.id());
        assertEquals("Teste", result.title());
    }

    @Test
    @DisplayName("Should throw exception when trying to finish a task with pendent subtasks")
    void updateTaskStatus_ShouldThrowException_WhenSubtasksPending() {
        Task parentTask = new Task();
        parentTask.setId(1L);
        parentTask.setUser(user);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));


        when(taskRepository.findById(1L)).thenReturn(Optional.of(parentTask));

        when(taskRepository.existsByParentTaskAndStatusNot(parentTask, TaskStatus.COMPLETED)).thenReturn(true);

        assertThrows(PendentSubtaskException.class, () -> {
            taskService.updateTask(1L, TaskStatus.COMPLETED, user.getUsername());
        });
    }
}
