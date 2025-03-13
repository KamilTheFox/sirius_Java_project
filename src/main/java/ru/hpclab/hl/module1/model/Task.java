package ru.hpclab.hl.module1.model;
import java.util.UUID;
import java.time.LocalDateTime;

public class Task {
    private UUID identifier;
    private String name;
    private String description;
    private User author;
    private User executor;
    private LocalDateTime deadline;
    private TaskStatus status = TaskStatus.NEW;

    public Task(UUID identifier, String name, String description,
                User author, User executor, LocalDateTime deadline) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.author = author;
        this.executor = executor;
        this.deadline = deadline;
    }

    public Task() {
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "identifier=" + identifier +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", executor=" + executor +
                ", deadline=" + deadline +
                ", status=" + status +
                '}';
    }
}