package com.example.taskscheduling.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.example.taskscheduling.model.Priority;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskRequest {

    @NotBlank(message = "title is required")
    @Size(max = 100, message = "title must be at most 100 characters")
    private String title;

    @Size(max = 500, message = "description must be at most 500 characters")
    private String description;

    @NotNull(message = "priority is required")
    private Priority priority;

}
