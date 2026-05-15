package com.taskscheduling.api.repository;
import com.taskscheduling.api.model.Task;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class InMemoryTaskRepository implements TaskRepository {
    private final Map<String, Task> tasks = new HashMap<>();

    @Override
    public Task save(Task task) {

        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(String id) {

        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {

        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteById(String id) {

        tasks.remove(id);
    }
}
