package br.com.moreira.desafiopetize.domain.repositories;

import br.com.moreira.desafiopetize.domain.entities.Task;
import br.com.moreira.desafiopetize.domain.entities.User;
import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByTitleAndDueDate(String title, LocalDate dueDate);

    List<Task> findByUser(User user);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END FROM Task t WHERE t.parentTask = :parentTask AND t.status <> :status")
    boolean existsByParentTaskAndStatusNot(@Param("parentTask") Task parentTask, @Param("status") TaskStatus status);

    @Query("SELECT t FROM Task t " +
            "LEFT JOIN FETCH t.subTask " +
            "LEFT JOIN FETCH t.attachments " +
            "WHERE t.id = :id")
    Optional<Task> findByIdWithDetails(@Param("id") Long id);

}