package br.com.moreira.desafiopetize.domain.services;

import br.com.moreira.desafiopetize.domain.entities.Attachment;
import br.com.moreira.desafiopetize.domain.entities.Task;
import br.com.moreira.desafiopetize.domain.entities.User;
import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import br.com.moreira.desafiopetize.domain.repositories.AttachmentRepository;
import br.com.moreira.desafiopetize.domain.repositories.TaskRepository;
import br.com.moreira.desafiopetize.domain.repositories.UserRepository;
import br.com.moreira.desafiopetize.domain.services.mapper.TaskMapper;
import br.com.moreira.desafiopetize.exceptions.*;
import br.com.moreira.desafiopetize.interfaces.dtos.CreateSubTaskDTO;
import br.com.moreira.desafiopetize.interfaces.dtos.CreateTaskRequestDto;
import br.com.moreira.desafiopetize.interfaces.dtos.TaskResponseDTO;
import br.com.moreira.desafiopetize.interfaces.dtos.UpdateTaskStatusRequestDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {


    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final FileStorageService fileStorageService;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, FileStorageService fileStorageService, AttachmentRepository attachmentRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.fileStorageService = fileStorageService;
        this.attachmentRepository = attachmentRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public TaskResponseDTO createTask(@Valid CreateTaskRequestDto dto, String username) {

        User loggedUser = findUserByUsername(username);

        Task newTask = taskMapper.toEntity(dto);

        newTask.setStatus(TaskStatus.PENDENT);
        newTask.setUser(loggedUser);
        newTask.setParentTask(null);

        validateEntity(newTask);

        Task savedTask = taskRepository.save(newTask);

        return taskMapper.toResponseDTO(savedTask);
    }



    @Transactional
    public TaskResponseDTO createSubtask(Long parentId, @Valid CreateSubTaskDTO dto, String username) {

        User loggedUser = findUserByUsername(username);

        Task parentTask = taskRepository.findById(parentId)
                .orElseThrow(() -> new ParentTaskNotFoundException("Task parent not found."));


        Task newSubTask = taskMapper.toEntity(dto);

        newSubTask.setStatus(TaskStatus.PENDENT);
        newSubTask.setUser(loggedUser);
        newSubTask.setParentTask(parentTask);

        Task savedSubtask = taskRepository.save(newSubTask);

        return taskMapper.toResponseDTO(savedSubtask);

    }

    public Page<TaskResponseDTO> listTask(TaskStatus status, Integer priority,
                                          LocalDate dueDate, String username,
                                          Pageable pageable) {

        User loggedUser = findUserByUsername(username);


        Task exampleTask = new Task();
        exampleTask.setStatus(status);
        exampleTask.setPriority(priority);
        exampleTask.setDueDate(dueDate);

        exampleTask.setUser(loggedUser);

        Example<Task> example = Example.of(exampleTask);

        Page<Task> tasksPage = taskRepository.findAll(example, pageable);

        return tasksPage.map(taskMapper::toResponseDTO);

    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskStatus newStatus, String username) {
        User loggedUser = findUserByUsername(username);


        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));

        if (!task.getUser().getId().equals(loggedUser.getId())) {
            throw new AccessDeniedException("You don't have permission to access this task.");
        }

        if (newStatus == TaskStatus.COMPLETED) {

            boolean hasPendentTask = taskRepository.existsByParentTaskAndStatusNot(
                    task,
                    TaskStatus.COMPLETED
            );

            if (hasPendentTask) {
                //arrumar excep
                throw new PendentSubtaskException("You cannot complete this task because it has pending subtasks.");
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

    public TaskResponseDTO findTaskById(Long id, String username) {
        User loggedUser = findUserByUsername(username);

        Task task = taskRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));

        if (!task.getUser().getId().equals(loggedUser.getId())) {
            throw new AccessDeniedException("You don't have permission to access this task.");
        }

        return taskMapper.toResponseDTO(task);
    }

    public void deleteTask(Long id, String username) {
        User loggedUser = findUserByUsername(username);


        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));

        if (!task.getUser().getId().equals(loggedUser.getId())) {
            throw new AccessDeniedException("You don't have access to delete this task.");
        }

        taskRepository.deleteById(id);
    }

    @Transactional
    public void storeAttachment(Long taskId, MultipartFile file, String username) {
        User loggedUser = findUserByUsername(username);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (!task.getUser().getId().equals(loggedUser.getId())) {
            throw new AccessDeniedException("You don't have permission to upload files to this task.");
        }

        String fileName = fileStorageService.storeFile(file);
        String filePath = Paths.get(fileName).toString();

        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(file.getContentType());
        attachment.setSize(file.getSize());
        attachment.setFilePath(filePath);
        attachment.setTask(task);

        attachmentRepository.save(attachment);

    }

    private User findUserByUsername(String username) {
        return (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
