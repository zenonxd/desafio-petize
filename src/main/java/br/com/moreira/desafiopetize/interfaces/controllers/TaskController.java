package br.com.moreira.desafiopetize.interfaces.controllers;

import br.com.moreira.desafiopetize.domain.entities.User;
import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import br.com.moreira.desafiopetize.domain.services.TaskService;
import br.com.moreira.desafiopetize.interfaces.dtos.CreateSubTaskDTO;
import br.com.moreira.desafiopetize.interfaces.dtos.CreateTaskRequestDto;
import br.com.moreira.desafiopetize.interfaces.dtos.TaskResponseDTO;
import br.com.moreira.desafiopetize.interfaces.dtos.UpdateTaskStatusRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Create a new task to a User that has authorization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
    })
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskRequestDto dto,
                                                      Authentication authentication) {

        User loggedUser = (User) authentication.getPrincipal();

        TaskResponseDTO newTask = taskService.createTask(dto, loggedUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTask.id())
                .toUri();

        return ResponseEntity.created(location).body(newTask);
    }

    @PostMapping("/{parentId}/subtasks")
    @Operation(summary = "Create a new subtask", description = "Create a new subtask, associating to a parent task. Need authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subtask created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Data."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "Parent Task not found."),
    })
    public ResponseEntity<TaskResponseDTO> createSubtask(
            @PathVariable Long parentId,
            @Valid @RequestBody CreateSubTaskDTO dto,
            Authentication authentication) {

        User loggedUser = (User) authentication.getPrincipal();

        TaskResponseDTO newSubtask = taskService.createSubtask(parentId, dto, loggedUser);


        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/tasks/{id}")
                .buildAndExpand(newSubtask.id())
                .toUri();

        return ResponseEntity.created(location).body(newSubtask);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> listTask(
            Authentication authentication,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dueDate,
            Pageable pageable) {

        User loggedUser = (User) authentication.getPrincipal();

        Page<TaskResponseDTO> tasks = taskService.listTask(status, priority, dueDate, loggedUser, pageable);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findTaskById(@PathVariable Long id) {
        TaskResponseDTO task = taskService.findTaskById(id);

        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateTaskStatusRequestDTO dto) {

        TaskResponseDTO updatedTask = taskService.updateTask(id, dto.status());

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/attachments")
    public ResponseEntity<String> uploadAttachment(@PathVariable Long id,
                                                   @RequestParam("file")MultipartFile file,
                                                   Authentication authentication) {
        User loggedUser = (User) authentication.getPrincipal();

        taskService.storeAttachment(id, file, loggedUser);

        return ResponseEntity.ok("File uploaded successfully.");
    }

}
