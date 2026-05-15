package com.taskscheduling.api.repository;

import com.taskscheduling.api.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(String id);

    List<Task> findAll();

    void deleteById(String id);
}
