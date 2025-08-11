package br.com.moreira.desafiopetize.domain.entities;

import br.com.moreira.desafiopetize.domain.enums.TaskStatus;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tb_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Enum<TaskStatus> status;
    private Integer priority;
    private Date dueDate;

    public Task() {}



    public Task(String title, String description, Enum<TaskStatus> status, Integer priority, Date dueDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Enum<TaskStatus> getStatus() {
        return status;
    }

    public void setStatus(Enum<TaskStatus> status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
