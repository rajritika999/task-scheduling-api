package com.example.taskscheduling.dto;

import com.example.taskscheduling.model.Priority;
import com.example.taskscheduling.model.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;

    public TaskResponse(Long id,
                        String title,
                        String description,
                        Priority priority,
                        Status status,
                        Instant createdAt,
                        Instant updatedAt) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
