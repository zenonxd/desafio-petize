package br.com.moreira.desafiopetize.interfaces.controllers;

import br.com.moreira.desafiopetize.domain.services.TaskService;
import br.com.moreira.desafiopetize.interfaces.dtos.CreateTaskRequestDto;
import br.com.moreira.desafiopetize.interfaces.dtos.TaskResponseDTO;
import br.com.moreira.desafiopetize.interfaces.dtos.UpdateTaskStatusRequestDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskRequestDto dto) {
        TaskResponseDTO newTask = taskService.createTask(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTask.id())
                .toUri();

        return ResponseEntity.created(location).body(newTask);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> listTask(Pageable pageable) {
        Page<TaskResponseDTO> tasks = taskService.listTask(pageable);

        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateTaskStatusRequestDTO dto) {
        TaskResponseDTO updatedTask = taskService.updateTask(id, dto);

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }

}
