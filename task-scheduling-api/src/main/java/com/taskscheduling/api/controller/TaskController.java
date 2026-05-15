package com.taskscheduling.api.controller;
import com.taskscheduling.api.dto.CreateTaskRequest;
import com.taskscheduling.api.dto.TaskResponse;
import com.taskscheduling.api.dto.UpdateTaskRequest;
import com.taskscheduling.api.model.TaskStatus;
import com.taskscheduling.api.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Validated
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(@PathVariable String id) {
        return taskService.getTaskById(id);
    }

    @GetMapping
    public List<TaskResponse> getAllTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be 0 or greater") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be 1 or greater") int size) {
        return taskService.getAllTasks(status, page, size);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable String id,
                                   @Valid @RequestBody UpdateTaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
    }
}

