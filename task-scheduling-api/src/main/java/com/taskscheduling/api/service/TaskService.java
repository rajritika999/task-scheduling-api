package com.taskscheduling.api.service;

import java.util.List;
import com.taskscheduling.api.dto.CreateTaskRequest;
import com.taskscheduling.api.dto.TaskResponse;
import com.taskscheduling.api.dto.UpdateTaskRequest;
import com.taskscheduling.api.model.TaskStatus;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request);

    TaskResponse getTaskById(String id);

    TaskResponse updateTask(String id, UpdateTaskRequest request);

    List<TaskResponse> getAllTasks(TaskStatus status, int page, int size);

    void deleteTask(String id);
}
