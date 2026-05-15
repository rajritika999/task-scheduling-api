package com.taskscheduling.api.dto;

import java.time.LocalDate;
import com.taskscheduling.api.model.TaskStatus;
import jakarta.validation.constraints.Future;
import lombok.Data;

@Data

public class UpdateTaskRequest {

    private String title;

    private String description;

    private TaskStatus status;

    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;
    
}
