package com.example.taskscheduling.repository;
import com.example.taskscheduling.model.Task;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Repository
public class TaskRepository {

    private final Map<Long, Task> taskStore = new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idGenerator.getAndIncrement());
        }
        taskStore.put(task.getId(), task);
        return task;
    }

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(taskStore.get(id));
    }

    public List<Task> findAll() {
        return new ArrayList<>(taskStore.values());
    }
}


