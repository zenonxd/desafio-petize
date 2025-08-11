package br.com.moreira.desafiopetize.domain.repositories;

import br.com.moreira.desafiopetize.domain.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByTitleAndDueDate(String title, Date dueDate);
}