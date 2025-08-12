package br.com.moreira.desafiopetize.domain.repositories;

import br.com.moreira.desafiopetize.domain.entities.Task;
import br.com.moreira.desafiopetize.domain.entities.User;
import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByTitleAndDueDate(String title, LocalDate dueDate);

    List<Task> findByUser(User user);

    boolean existsByParentTaskAndStatusNot(Task parentTask, TaskStatus status);
}