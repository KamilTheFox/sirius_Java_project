package ru.hpclab.hl.module1.service;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.model.Task;

import java.util.*;

@Service
public class TaskService {
    private final Map<UUID, Task> tasks = new HashMap<>();

    public Task createTask(Task task) {
        task.setIdentifier(UUID.randomUUID());
        tasks.put(task.getIdentifier(), task);
        return task;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task getTaskById(UUID id) {
        return tasks.get(id);
    }

    public Task updateTask(UUID id, Task task) {
        if (tasks.containsKey(id)) {
            task.setIdentifier(id);
            tasks.put(id, task);
            return task;
        }
        return null;
    }

    public boolean deleteTask(UUID id) {
        return tasks.remove(id) != null;
    }
}