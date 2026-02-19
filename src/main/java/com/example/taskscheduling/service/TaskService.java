package com.example.taskscheduling.service;

import com.example.taskscheduling.dto.TaskRequest;
import com.example.taskscheduling.dto.TaskResponse;
import com.example.taskscheduling.exception.TaskNotFoundException;
import com.example.taskscheduling.model.*;
import com.example.taskscheduling.repository.TaskRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // CREATE
    public TaskResponse createTask(TaskRequest request) {

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(Status.PENDING);
        task.setDeleted(false);
        task.setCreatedAt(Instant.now());

        Task saved = taskRepository.save(task);

        return mapToResponse(saved);
    }

    // GET BY ID
    public TaskResponse getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found with id: " + id));

        return mapToResponse(task);
    }

    // GET ALL
    public List<TaskResponse> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .filter(t -> !t.isDeleted())
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // UPDATE
    public TaskResponse updateTask(Long id, TaskRequest request) {

        Task existing = taskRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found with id: " + id));

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setPriority(request.getPriority());
        existing.setUpdatedAt(Instant.now());

        Task updated = taskRepository.save(existing);

        return mapToResponse(updated);
    }

    // SOFT DELETE
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found with id: " + id));

        task.setDeleted(true);
        task.setUpdatedAt(Instant.now());

        taskRepository.save(task);
    }

    // UPDATE STATUS
    public TaskResponse updateStatus(Long id, Status newStatus) {

        Task task = taskRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found with id: " + id));

        validateStatusTransition(task.getStatus(), newStatus);

        task.setStatus(newStatus);
        task.setUpdatedAt(Instant.now());

        Task updated = taskRepository.save(task);

        return mapToResponse(updated);
    }

    // STATUS VALIDATION
    private void validateStatusTransition(Status current, Status next) {

        if (current == Status.PENDING && next == Status.COMPLETED) {
            return;
        }

        if (current == Status.COMPLETED && next == Status.PENDING) {
            return;
        }

        throw new IllegalStateException("Invalid status transition");
    }

    // ENTITY → DTO
    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
