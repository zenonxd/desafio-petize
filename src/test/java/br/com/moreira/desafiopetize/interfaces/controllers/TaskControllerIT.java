package br.com.moreira.desafiopetize.interfaces.controllers;


import br.com.moreira.desafiopetize.domain.entities.Task;
import br.com.moreira.desafiopetize.domain.entities.User;
import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import br.com.moreira.desafiopetize.domain.repositories.TaskRepository;
import br.com.moreira.desafiopetize.domain.repositories.UserRepository;
import br.com.moreira.desafiopetize.exceptions.UsernameNotFoundException;
import br.com.moreira.desafiopetize.interfaces.dtos.CreateTaskRequestDto;
import br.com.moreira.desafiopetize.interfaces.dtos.UpdateTaskStatusRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should create a parent task and return 201 created")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createTask_ShouldReturn201_WhenSuccessful() throws Exception {
        CreateTaskRequestDto createTaskDTO = new CreateTaskRequestDto(
                "Integration Task",
                "Task Description",
                1,
                LocalDate.now().plusDays(5)
        );

        String requestBodyJson = objectMapper.writeValueAsString(createTaskDTO);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))

                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Integration Task"));

    }

    @Test
    @DisplayName("Should return 400 bad request when trying to conclude a parent task that has subtasks pending")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateTaskStatus_ShouldReturn400_WhenSubtasksPending() throws Exception {


        User adminUser = userRepository.findByUsername("admin")
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Task parentTask = new Task();
        parentTask.setTitle("Parent Task");
        parentTask.setUser(adminUser);
        parentTask.setStatus(TaskStatus.PENDENT);
        taskRepository.save(parentTask);

        Task subTask = new Task();
        subTask.setTitle("Subtask");
        subTask.setParentTask(parentTask);
        subTask.setUser(adminUser);
        subTask.setStatus(TaskStatus.PENDENT);
        taskRepository.save(subTask);

        UpdateTaskStatusRequestDTO requestDTO = new UpdateTaskStatusRequestDTO(TaskStatus.COMPLETED);
        String requestBodyJson = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(patch("/api/tasks/" + parentTask.getId() + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andExpect(status().isBadRequest());
    }
}
