package com.taskscheduling.api.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Task {

    private String id;

    private String title;

    private String description;

    private TaskStatus status;

    private LocalDate dueDate;
    
}
