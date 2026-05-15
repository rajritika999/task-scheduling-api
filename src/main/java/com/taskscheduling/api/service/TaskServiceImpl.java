package com.taskscheduling.api.service;

import com.taskscheduling.api.dto.CreateTaskRequest;
import com.taskscheduling.api.dto.TaskResponse;
import com.taskscheduling.api.dto.UpdateTaskRequest;
import com.taskscheduling.api.exception.TaskNotFoundException;
import com.taskscheduling.api.model.Task;
import com.taskscheduling.api.model.TaskStatus;
import com.taskscheduling.api.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {

        Task task = new Task();

        task.setId(UUID.randomUUID().toString());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        } else {
            task.setStatus(TaskStatus.PENDING);
        }

        taskRepository.save(task);

        return mapToResponse(task);
    }

    @Override
    public TaskResponse getTaskById(String id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found with id: " + id));

        return mapToResponse(task);
    }

    @Override
    public List<TaskResponse> getAllTasks(TaskStatus status, int page, int size) {
        List<Task> tasks = taskRepository.findAll();

        if (status != null) {
            tasks = tasks.stream()
                    .filter(task -> status.equals(task.getStatus()))
                    .collect(Collectors.toList());
        }

        tasks.sort(Comparator.comparing(Task::getDueDate));

        int fromIndex = page * size;
        if (fromIndex >= tasks.size()) {
            return new ArrayList<>();
        }

        int toIndex = Math.min(fromIndex + size, tasks.size());
        List<Task> pageTasks = tasks.subList(fromIndex, toIndex);

        List<TaskResponse> responseList = new ArrayList<>();
        for (Task task : pageTasks) {
            responseList.add(mapToResponse(task));
        }

        return responseList;
    }

    @Override
    public TaskResponse updateTask(String id, UpdateTaskRequest request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found with id: " + id));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }

        taskRepository.save(task);

        return mapToResponse(task);
    }

    @Override
    public void deleteTask(String id) {

        taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found with id: " + id));

        taskRepository.deleteById(id);
    }

    private TaskResponse mapToResponse(Task task) {

        TaskResponse response = new TaskResponse();

        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setDueDate(task.getDueDate());

        return response;
    }
}