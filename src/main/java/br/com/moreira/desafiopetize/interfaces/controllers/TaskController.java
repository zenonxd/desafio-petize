package br.com.moreira.desafiopetize.interfaces.controllers;

import br.com.moreira.desafiopetize.domain.entities.User;
import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import br.com.moreira.desafiopetize.domain.services.TaskService;
import br.com.moreira.desafiopetize.interfaces.dtos.CreateSubTaskDTO;
import br.com.moreira.desafiopetize.interfaces.dtos.CreateTaskRequestDto;
import br.com.moreira.desafiopetize.interfaces.dtos.TaskResponseDTO;
import br.com.moreira.desafiopetize.interfaces.dtos.UpdateTaskStatusRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
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
    @Operation(summary = "Create a new parent task.", description = "Create a new task to a User that has authorization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid data."),
            @ApiResponse(responseCode = "403", description = "Unauthorized."),
    })
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskRequestDto dto,
                                                      Authentication authentication) {

        String loggedUser = authentication.getName();

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

        String loggedUser = authentication.getName();

        TaskResponseDTO newSubtask = taskService.createSubtask(parentId, dto, loggedUser);


        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/tasks/{id}")
                .buildAndExpand(newSubtask.id())
                .toUri();

        return ResponseEntity.created(location).body(newSubtask);
    }


    @GetMapping
    @Operation(summary = "List the user tasks.", description = "Returns a pageable list that belongs to the User, with optional filters.")
    public ResponseEntity<Page<TaskResponseDTO>> listTask(
            Authentication authentication,
            @Parameter(description = "Filter per status") @RequestParam(required = false) TaskStatus status,
            @Parameter(description = "Filter per priority") @RequestParam(required = false) Integer priority,
            @Parameter(description = "Filter per due date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
            Pageable pageable) {

        String loggedUser = authentication.getName();

        Page<TaskResponseDTO> tasks = taskService.listTask(status, priority, dueDate, loggedUser, pageable);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Search a task by ID.", description = "Returns a task that belongs to the User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "Task not found."),
    })
    public ResponseEntity<TaskResponseDTO> findTaskById(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();


        TaskResponseDTO task = taskService.findTaskById(id, username);

        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update the task status.", description = "Needs Auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid Data."),
            @ApiResponse(responseCode = "403", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "Task not found."),
    })
    public ResponseEntity<TaskResponseDTO> updateTask(
            @Parameter(description = "Task ID to be updated") @PathVariable Long id,
            @Valid @RequestBody UpdateTaskStatusRequestDTO dto,
            Authentication authentication) {

        String username = authentication.getName();


        TaskResponseDTO updatedTask = taskService.updateTask(id, dto.status(), username);

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a task.", description = "Needs Auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully."),
            @ApiResponse(responseCode = "403", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "Task not found."),
    })
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();


        taskService.deleteTask(id, username);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/attachments")
    @Operation(summary = "Upload a file to a task", description = "Needs Auth")
    public ResponseEntity<String> uploadAttachment(@PathVariable Long id,
                                                   @RequestParam("file")MultipartFile file,
                                                   Authentication authentication) {

        String loggedUser = authentication.getName();

        taskService.storeAttachment(id, file, loggedUser);

        return ResponseEntity.ok("File uploaded successfully.");
    }

}
